#!/usr/bin/env python3
'''
    books_api.py
    Jeff Ondich, 25 April 2016
    Updated 3 May 2018

    Simple Flask API used in the sample web app for
    CS 257, Spring 2016-2018. This is the Flask app for the
    "books and authors" API only. There's a separate Flask app
    for the books/authors website.
'''
import sys
import flask
import json
import config
import psycopg2

app = flask.Flask(__name__, static_folder='static', template_folder='templates')

def get_connection():
    '''
    Returns a connection to the database described
    in the config module. Returns None if the
    connection attempt fails.
    '''
    connection = None
    try:
        connection = psycopg2.connect(database=config.database,
                                      user=config.user,
                                      password=config.password)
    except Exception as e:
        print(e, file=sys.stderr)
    return connection

def get_select_query_results(connection, query, parameters=None):
    '''
    Executes the specified query with the specified tuple of
    parameters. Returns a cursor for the query results.

    Raises an exception if the query fails for any reason.
    '''
    cursor = connection.cursor()
    if parameters is not None:
        cursor.execute(query, parameters)
    else:
        cursor.execute(query)
    return cursor

@app.after_request
def set_headers(response):
    response.headers['Access-Control-Allow-Origin'] = '*'
    return response

@app.route('/authors/') 
def get_authors():
    '''
    Returns a list of all the authors in our database. See
    get_author_by_id below for description of the author
    resource representation.

    By default, the list is presented in alphabetical order
    by last name, then first_name. You may, however, use
    the GET parameter sort to request sorting by birth year.

        http://.../authors/?sort=birth_year

    Returns an empty list if there's any database failure.
    '''
    
    query = '''SELECT id, first_name, last_name, birth_year, death_year
               FROM authors ORDER BY '''

    sort_argument = flask.request.args.get('sort')
    if sort_argument == 'birth_year':
        query += 'birth_year'
    else:
        query += 'last_name, first_name'

    author_list = []
    connection = get_connection()
    if connection is not None:
        try:
            for row in get_select_query_results(connection, query):
                author = {'id':row[0],
                          'first_name':row[1], 'last_name':row[2],
                          'birth_year':row[3], 'death_year':row[4]}
                author_list.append(author)
        except Exception as e:
            print(e, file=sys.stderr)
        connection.close()

    return json.dumps(author_list)

@app.route('/authors/<author_last_name>')
def get_authors_by_last_name(author_last_name):
    '''
    Returns a list of all the authors with last names containing
    the specified last name. The match is case-insensitive.
    
    See get_author_by_id below for a description of the
    author resource representation.
    '''
    query = '''SELECT id, first_name, last_name, birth_year, death_year
               FROM authors
               WHERE UPPER(last_name) LIKE '%%' || UPPER(%s) || '%%'
               ORDER BY last_name, first_name'''

    author_list = []
    connection = get_connection()
    if connection is not None:
        try:
            for row in get_select_query_results(connection, query, (author_last_name,)):
                author = {'id':row[0],
                          'first_name':row[1], 'last_name':row[2],
                          'birth_year':row[3], 'death_year':row[4]}
                author_list.append(author)
        except Exception as e:
            print(e, file=sys.stderr)
        connection.close()

    return json.dumps(author_list)

@app.route('/author/<author_id>')
def get_author_by_id(author_id):
    '''
    Returns the author resource for the author with the specified id.
    An author resource will be represented as a JSON dictionary
    with keys 'first_name' (string value), 'last_name' (string),
    'birth_year' (int), 'death_year' (int), and 'id' (int).

    Returns an empty dictionary if there's any database failure.
    '''
    query = '''SELECT id, first_name, last_name, birth_year, death_year
               FROM authors WHERE id = %s'''

    author = {}
    connection = get_connection()
    if connection is not None:
        try:
            cursor = get_select_query_results(connection, query, (author_id,))
            row = next(cursor)
            if row is not None:
                author = {'id':row[0],
                          'first_name':row[1], 'last_name':row[2],
                          'birth_year':row[3], 'death_year':row[4]}
        except Exception as e:
            print(e, file=sys.stderr)
        connection.close()

    return json.dumps(author)

@app.route('/books/')
def get_books():
    '''
    Returns the list of books in the database. A book resource
    will be represented by a JSON dictionary with keys 'id' (int),
    'title' (string), and 'publication_year' (int).

    Returns an empty list if there's any database failure.
    '''
    query = 'SELECT id, title, publication_year FROM books ORDER BY title'
    book_list = []
    connection = get_connection()
    if connection is not None:
        try:
            for row in get_select_query_results(connection, query):
                book = {'id':row[0], 'title':row[1], 'publication_year':row[2]}
                book_list.append(book)
        except Exception as e:
            print(e, file=sys.stderr)
        connection.close()

    return json.dumps(book_list)
    
@app.route('/book/<book_id>')
def get_book_by_id(book_id):
    '''
    Returns the book resource that has the specified id.
    See get_books for a description of the representation of a book
    resource.

    Returns an empty dictionary if there's any database failure.
    '''
    query = '''SELECT id, title, publication_year FROM books WHERE id = %s'''
    book = {}
    connection = get_connection()
    if connection is not None:
        try:
            cursor = get_select_query_results(connection, query, (book_id,))
            row = next(cursor)
            if row is not None:
                book = {'id':row[0], 'title':row[1], 'publication_year':row[2]}
        except Exception as e:
            print(e, file=sys.stderr)
        connection.close()

    return json.dumps(book)

@app.route('/books/author/<author_id>')
def get_books_for_author(author_id):
    query = '''SELECT books.id, books.title, books.publication_year
               FROM books, authors, books_authors
               WHERE books.id = books_authors.book_id
                 AND authors.id = books_authors.author_id
                 AND authors.id = %s
               ORDER BY books.publication_year'''
    book_list = []
    connection = get_connection()
    if connection is not None:
        try:
            for row in get_select_query_results(connection, query, (author_id,)):
                book = {'id':row[0], 'title':row[1], 'publication_year':row[2]}
                book_list.append(book)
        except Exception as e:
            print(e, file=sys.stderr)
        connection.close()

    return json.dumps(book_list)

@app.route('/authors/book/<book_id>')
def get_authors_for_book(book_id):
    query = '''SELECT authors.id, authors.first_name, authors.last_name,
                 authors.birth_year, authors.death_year
               FROM books, authors, books_authors
               WHERE books.id = books_authors.book_id
                 AND authors.id = books_authors.author_id
                 AND books.id = %s
               ORDER BY authors.last_name, authors.first_name'''
    author_list = []
    connection = get_connection()
    if connection is not None:
        try:
            for row in get_select_query_results(connection, query, (book_id,)):
                author = {'id':row[0],
                          'first_name':row[1], 'last_name':row[2],
                          'birth_year':row[3], 'death_year':row[4]}
                author_list.append(author)
        except Exception as e:
            print(e, file=sys.stderr)
        connection.close()

    return json.dumps(author_list)

@app.route('/help')
def help():
    rule_list = []
    for rule in app.url_map.iter_rules():
        rule_text = rule.rule.replace('<', '&lt;').replace('>', '&gt;')
        rule_list.append(rule_text)
    return json.dumps(rule_list)

if __name__ == '__main__':
    if len(sys.argv) != 3:
        print('Usage: {0} host port'.format(sys.argv[0]), file=sys.stderr)
        exit()

    host = sys.argv[1]
    port = sys.argv[2]
    app.run(host=host, port=int(port), debug=True)

