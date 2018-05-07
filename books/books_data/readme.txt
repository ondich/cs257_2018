Setting up the books/authors database
CS257 Software Design
Spring 2018
Jeff Ondich

1. Create the tables in your PostgreSQL database

    CREATE TABLE authors (
        id integer NOT NULL,
        last_name text,
        first_name text,
        birth_year integer,
        death_year integer
    );

    CREATE TABLE books (
        id integer NOT NULL,
        title text,
        publication_year integer
    );

    CREATE TABLE books_authors (
        book_id integer,
        author_id integer
    );

2. Load the data

    cd tables
    psql
      \copy authors from 'authors.csv' DELIMITER ',' CSV NULL AS 'NULL'
      \copy books from 'books.csv' DELIMITER ',' CSV NULL AS 'NULL'
      \copy books_authors from 'books_authors.csv' DELIMITER ',' CSV NULL AS 'NULL'

