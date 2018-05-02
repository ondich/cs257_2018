/*
 *  books.js
 *  Jeff Ondich, 27 April 2016
 *
 *  A little bit of Javascript showing one small example of AJAX
 *  within the "books and authors" sample for Carleton CS257,
 *  Spring Term 2017.
 */

function onAuthorsButton() {
    var url = api_base_url + 'authors/';
    xmlHttpRequest = new XMLHttpRequest();
    xmlHttpRequest.open('get', url);

    xmlHttpRequest.onreadystatechange = function() {
            if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) { 
                authorsCallback(xmlHttpRequest.responseText);
            } 
        }; 

    xmlHttpRequest.send(null);
}

function authorsCallback(responseText) {
    var authorsList = JSON.parse(responseText);
    var tableBody = '';
    for (var k = 0; k < authorsList.length; k++) {
        tableBody += '<tr>';

        tableBody += '<td><a onclick="getAuthor(' + authorsList[k]['author_id'] + ",'"
                            + authorsList[k]['first_name'] + ' ' + authorsList[k]['last_name'] + "')\">"
                            + authorsList[k]['last_name'] + ', '
                            + authorsList[k]['first_name'] + '</a></td>';

        tableBody += '<td>' + authorsList[k]['birth_year'] + '-';
        if (authorsList[k]['death_year'] != 0) {
            tableBody += authorsList[k]['death_year'];
        }
        tableBody += '</td>';
        tableBody += '</tr>';
    }

    var resultsTableElement = document.getElementById('results_table');
    resultsTableElement.innerHTML = tableBody;
}

function getAuthor(authorID, authorName) {
    var url = api_base_url + 'author/' + authorID + '/books/';
    xmlHttpRequest = new XMLHttpRequest();
    xmlHttpRequest.open('get', url);

    xmlHttpRequest.onreadystatechange = function() {
            if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) { 
                getBooksForAuthorCallback(authorName, xmlHttpRequest.responseText);
            } 
        }; 

    xmlHttpRequest.send(null);
}

function getBooksForAuthorCallback(authorName, responseText) {
    var booksList = JSON.parse(responseText);
    var tableBody = '<tr><th>' + authorName + '</th></tr>';
    for (var k = 0; k < booksList.length; k++) {
        tableBody += '<tr>';
        tableBody += '<td>' + booksList[k]['title'] + '</td>';
        tableBody += '<td>' + booksList[k]['publication_year'] + '</td>';
        tableBody += '</tr>';
    }

    var resultsTableElement = document.getElementById('results_table');
    resultsTableElement.innerHTML = tableBody;
}

