#!/usr/bin/env python3
'''
    csv_to_tables.py
    Jeff Ondich, 24 April 2017
    Updated 20 April 2018

    Sample code illustrating a simple conversion of the
    books & authors dataset represented as in books_and_authors.csv,
    into the books, authors, and books_authors tables (in
    CSV form).

    This is trickier than my json_to_tables.py example,
    because in the books.csv file, the authors are implicit
    in the list of books rather than being separated out
    into their own data structure as they are in the
    books_and_authors.json file.
'''
import sys
import re
import csv

def load_from_books_csv_file(csv_file_name):
    ''' Collect all the data from my sample books_and_authors.csv file,
        assembling it into a list of books, a dictionary of authors,
        and a list of book/author ID links. Rather than fully
        documenting the data structures built in this function and
        used in the later functions, I'm going to let you play around
        with it. I recommend just sticking some print statements
        in various places (or set some breakpoints if you use an IDE
        for Python, like PyCharm). You might find it interesting to
        figure out why I needed to use a dictionary for authors, but not
        for books.
    '''
    csv_file = open(csv_file_name)
    reader = csv.reader(csv_file)

    authors = {}
    books = []
    books_authors = []
    for row in reader:
        assert len(row) == 3
        book_id = len(books)
        book = {'id': book_id, 'title': row[0], 'publication_year': row[1]}
        books.append(book)
        for author in authors_from_authors_string(row[2]):
            if author in authors:
                author_id = authors[author]
            else:
                author_id = len(authors)
                authors[author] = author_id
            books_authors.append({'book_id': book_id, 'author_id': author_id})

    csv_file.close()
    return (books, authors, books_authors)

def authors_from_authors_string(authors_string):
    ''' Returns a list of authors based on an "authors string". Each author in
        the returned list is represented as a 4-tuple:

            (last_name, first_name, birth_year, death_year)
    
        The "authors string" will have the form
   
            FirstName LastName (BirthYear-DeathYear)

        where DeathYear can be the empty string and FirstName can be multiple
        names. The authors string can also have more than one author separated
        by " and ":

            FirstName LastName (BirthYear-DeathYear) and FirstName2 LastName2 (BirthYear2-DeathYear2)

        Obviously, this is a hack job, and will break pathetically in all sorts
        of situations (e.g. three authors using commas and only one " and ").
        But it works for my toy example (which contains exactly one co-written book).
    '''
    authors = []
    single_author_strings = authors_string.split(' and ')
    for single_author_string in single_author_strings:
        match = re.search(r'(.*) ([^ ]+) \(([0-9]+)-([0-9]*)\)', single_author_string)
        if match:
            first_name = match.group(1)
            last_name = match.group(2)
            birth_year = match.group(3)
            death_year = match.group(4)
            author = (last_name, first_name, birth_year, death_year)
            authors.append(author)
        else:
            print('Badly formatted authors string: {0}'.format(authors_string), file=sys.stderr)

    return authors

def save_books_table(books, csv_file_name):
    ''' Save the books in CSV form, with each row containing
        (id, title, publication year). '''
    output_file = open(csv_file_name, 'w')
    writer = csv.writer(output_file)
    for book in books:
        book_row = [book['id'], book['title'], book['publication_year']]
        writer.writerow(book_row)
    output_file.close()

def save_authors_table(authors, csv_file_name):
    ''' Save the books in CSV form, with each row containing
        (id, last name, first name, birth year, death year), where
        death year can be NULL. '''
    output_file = open(csv_file_name, 'w')
    writer = csv.writer(output_file)
    for author in sorted(authors, key=authors.get):
        (last_name, first_name, birth_year, death_year) = author
        if death_year == '':
            death_year = 'NULL'
        author_id = authors[author]
        author_row = [author_id, last_name, first_name, birth_year, death_year]
        writer.writerow(author_row)
    output_file.close()

def save_linking_table(books_authors, csv_file_name):
    ''' Save the books in CSV form, with each row containing
        (book id, author id). '''
    output_file = open(csv_file_name, 'w')
    writer = csv.writer(output_file)
    for book_author in books_authors:
        books_authors_row = [book_author['book_id'], book_author['author_id']]
        writer.writerow(books_authors_row)
    output_file.close()

if __name__ == '__main__':
    books, authors, books_authors = load_from_books_csv_file('books_and_authors.csv')

    print('Saving books table to books.csv')
    save_books_table(books, 'books.csv')
    print('Saving authors table to authors.csv')
    save_authors_table(authors, 'authors.csv')
    print('Saving books_authors table to books_authors.csv')
    save_linking_table(books_authors, 'books_authors.csv')

