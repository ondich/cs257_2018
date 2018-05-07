#!/usr/bin/env python3
'''
    json_to_tables.py
    Jeff Ondich, 24 April 2017

    Sample code illustrating a simple conversion of the
    books & authors dataset represented as in books.json,
    into the books, authors, and books_authors tables (in
    CSV form).
'''
import sys
import json
import csv

def save_books_table_as_csv(books, csv_file_name):
    output_file = open(csv_file_name, 'w')
    writer = csv.writer(output_file)
    for book in books:
        book_row = [book['id'], book['title'], book['publication_year']]
        writer.writerow(book_row)
    output_file.close()

def save_authors_table_as_csv(authors, csv_file_name):
    output_file = open(csv_file_name, 'w')
    writer = csv.writer(output_file)
    for author in authors:
        author_row = [author['id'], author['last_name'], author['first_name'], author['birth_year'], author['death_year']]
        writer.writerow(author_row)
    output_file.close()

def save_linking_table_as_csv(books, authors, csv_file_name):
    ''' Exercise for the reader. Roughly, you might do this like so:
        
        Open a csv writer
        Build a dictionary mapping (author name) --> (author id)
        for book in books:
            for author_name in book['authors']:
                author_id = ...
                table_row = [book['id'], author_id]
                writer.writerow(table_row)
    '''
    print('  This function is not yet implemented')

if __name__ == '__main__':
    # Turn JSON string into Python objects
    books_and_authors = json.loads(open('books_and_authors.json').read())

    # Extract the authors list and the books list from there
    authors = books_and_authors['authors']
    books = books_and_authors['books']

    # Save the tables
    print('Saving books table as books_table.csv')
    save_books_table_as_csv(books, 'books_table.csv')
    print('Saving authors table as authors_table.csv')
    save_authors_table_as_csv(authors, 'authors_table.csv')
    print('Saving books_authors table as books_authors_table.csv')
    save_linking_table_as_csv(books, authors, 'books_authors_table.csv')

