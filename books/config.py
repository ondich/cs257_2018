#!/usr/bin/env python
'''
    config.py
    Jeff Ondich, 25 April 2016

    Database login info for the "books and authors" sample web
    app for CS 257, Spring 2016.

    NOTE: I'M INCLUDING THIS FILE IN MY REPOSITORY TO SHOW YOU
    ITS FORMAT, BUT YOU SHOULD NOT KEEP CONFIG FILES WITH LOGIN
    CREDENTIALS IN YOUR REPOSITORY. In fact, I generally put a
    .gitignore file with "config.py" in it in whatever directory
    is supposed to house the config file. It's tricky to provide
    a config sample without accidentally pushing user names and
    passwords at a later time. Mostly, I try to illustrate the
    config file format in a readme file or in a code samples
    file, while .gitignore-ing the configs in the actual code
    directories.
'''

# Change these values as appropriate for your postgresql setup.
database = 'jondich'
user = 'jondich'
password = ''
