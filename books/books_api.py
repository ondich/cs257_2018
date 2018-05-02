#!/usr/bin/env python3
'''
    books_api.py
    Jeff Ondich, 25 April 2016

    Simple Flask API used in the sample web app for
    CS 257, Spring 2016-2017. This is the Flask app for the
    "books and authors" API only. There's a separate Flask app
    for the books/authors website.
'''
import sys
import flask
import json
import config
import psycopg2

app = flask.Flask(__name__, static_folder='static', template_folder='templates')

def _fetch_all_rows_for_query(query):
    '''
    Returns a list of rows obtained from the books database by the specified SQL
    query. If the query fails for any reason, an empty list is returned.

    Note that this is not necessarily the right error-handling choice. Would users
    of the API like to know the nature of the error? Do we as API implementors
    want to share that information? There are many considerations to balance.
    '''
    try:
        connection = psycopg2.connect(database=config.database, user=config.user, password=config.password)
    except Exception as e:
        print('Connection error:', e, file=sys.stderr)
        return []

    rows = []
    try:
        cursor = connection.cursor()
        cursor.execute(query)
        rows = cursor.fetchall() # This can be trouble if your query results are really big.
    except Exception as e:
        print('Error querying database:', e, file=sys.stderr)

    connection.close()
    return rows

@app.after_request
def set_headers(response):
    response.headers['Access-Control-Allow-Origin'] = '*'
    return response

# @ symbol indicates a "python decorator"
@app.route('/authors/') 
def get_authors():
    '''
    Returns a list of all the authors in our database, in alphabetical
    order by last name, then first_name. See get_author_by_id below
    for description of the author resource representation.

        http://.../authors/
        http://.../authors/?sort=last_name
        http://.../authors/?sort=birth_year
    '''
    
    query = '''SELECT id, first_name, last_name, birth_year, death_year
               FROM authors ORDER BY '''
               
    sort_argument = flask.request.args.get('sort')
    if sort_argument == 'birth_year':
        query += 'birth_year'
    else:
        query += 'last_name, first_name'

    author_list = []
    for row in _fetch_all_rows_for_query(query):
        url = flask.url_for('get_author_by_id', author_id=row[0], _external=True)
        author = {'author_id':row[0], 'first_name':row[1], 'last_name':row[2],
                  'birth_year':row[3], 'death_year':row[4], 'url':url}
        author_list.append(author)

    return json.dumps(author_list)

@app.route('/authors/<author_last_name>')
def get_authors_by_last_name(author_last_name):
    '''
    Returns a list of all the authors with last names equal to
    (case-insensitive) the specified last name.  See get_author_by_id
    below for description of the author resource representation.
    '''
    query = '''SELECT id, first_name, last_name, birth_year, death_year
               FROM authors
               WHERE UPPER(last_name) LIKE UPPER('%{0}%')
               ORDER BY last_name, first_name'''.format(author_last_name)

    author_list = []
    for row in _fetch_all_rows_for_query(query):
        url = flask.url_for('get_author_by_id', author_id=row[0], _external=True)
        author = {'author_id':row[0], 'first_name':row[1], 'last_name':row[2],
                  'birth_year':row[3], 'death_year':row[4], 'url':url}
        author_list.append(author)

    return json.dumps(author_list)

@app.route('/author/<author_id>')
def get_author_by_id(author_id):
    '''
    Returns the author resource that has the specified id.
    An author resource will be represented as a JSON dictionary
    with keys 'first_name' (string value), 'last_name' (string),
    'birth_year' (int), 'death_year' (int), 'author_id' (int),
    and 'url' (string). The value associated with 'url' is a URL
    you can use to retrieve this same author in the future.
    '''
    query = '''SELECT id, first_name, last_name, birth_year, death_year
               FROM authors WHERE id = {0}'''.format(author_id)

    rows = _fetch_all_rows_for_query(query)
    if len(rows) > 0:
        row = rows[0]
        url = flask.url_for('get_author_by_id', author_id=row[0], _external=True)
        author = {'author_id':row[0], 'first_name':row[1], 'last_name':row[2],
                  'birth_year':row[3], 'death_year':row[4], 'url':url}
        return json.dumps(author)

    return json.dumps({})

@app.route('/books/')
def get_books():
    '''
    Returns the list of books in the database. A book resource
    will be represented by a JSON dictionary with keys 'title' (string),
    'publication_year' (int), and 'url' (string). The value
    associated with 'url' is a URL you can use to retrieve this
    same book in the future.
    '''
    query = 'SELECT id, title, publication_year FROM books ORDER BY title'
    book_list = []
    for row in _fetch_all_rows_for_query(query):
        url = flask.url_for('get_book_by_id', book_id=row[0], _external=True)
        book = {'book_id':row[0], 'title':row[1], 'publication_year':row[2], 'url':url}
        book_list.append(book)

    return json.dumps(book_list)
    
@app.route('/book/<book_id>')
def get_book_by_id(book_id):
    '''
    Returns the book resource that has the specified id.
    See get_books for a description of the representation of a book
    resource.
    '''
    query = '''SELECT id, title, publication_year FROM books WHERE id = {0}'''.format(book_id) 
    rows = _fetch_all_rows_for_query(query)
    if len(rows) > 0:
        row = rows[0]
        url = flask.url_for('get_book_by_id', book_id=row[0], _external=True)
        book = {'book_id':row[0], 'title':row[1], 'publication_year':row[2], 'url':url}
        return json.dumps(book)

    return json.dumps({})

@app.route('/author/<author_id>/books/')
def get_books_for_author(author_id):
    query = '''SELECT books.id, books.title, books.publication_year
               FROM books, authors, books_authors
               WHERE books.id = books_authors.book_id
                 AND authors.id = books_authors.author_id
                 AND authors.id = {0}
               ORDER BY books.publication_year'''.format(author_id)
    book_list = []
    for row in _fetch_all_rows_for_query(query):
        url = flask.url_for('get_book_by_id', book_id=row[0], _external=True)
        book = {'book_id':row[0], 'title':row[1], 'publication_year':row[2], 'url':url}
        book_list.append(book)

    return json.dumps(book_list)

@app.route('/book/<book_id>/authors/')
def get_authors_for_book(book_id):
    query = '''SELECT authors.id, authors.first_name, authors.last_name,
                 authors.birth_year, authors.death_year
               FROM books, authors, books_authors
               WHERE books.id = books_authors.book_id
                 AND authors.id = books_authors.author_id
                 AND books.id = {0}
               ORDER BY authors.last_name, authors.first_name'''.format(book_id)
    author_list = []
    for row in _fetch_all_rows_for_query(query):
        url = flask.url_for('get_author_by_id', author_id=row[0], _external=True)
        author = {'author_id':row[0], 'first_name':row[1], 'last_name':row[2],
                  'birth_year':row[3], 'death_year':row[4], 'url':url}
        author_list.append(author)

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

