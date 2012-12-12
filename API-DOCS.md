# Crosscram Web Serice API Documentation

## Creating a Game

Send an HTTP POST request to the "/game" URI.  Two pieces of information are necessary for the server to complete this request: the dimensions of the game board and the two bots to play the game.  There are two ways in which each of these values can be set.

* Board dimensions:
    * dims: the dimensions of the board are encoded as a comma-separated list of integers between square brackets. For example, dims=[10,20] encodes a board of ten rows and twenty columns. Only two-dimensional boards are currently supported.
    * rows, cols: each dimension is listed separately. If either dimension is not set when using these parameters, a 400 response is returned.
* Bots:
    * bots: the bots are similarly encoded as a comma-separated list of namespace names between square brackets.  For example, bots=[crosscram.samples.windowshade-rand,crosscram.samples.reserves-move].
    * bot1, bot2: the bots are listed separately. If either bot is not set when using these parameters, a 400 response is returned.

If this request is successful, a redirect response (303) will be sent containing the URI of the created game resource.  If unsuccessful, a 400 response is sent.

For example, curl can be used to create a game resource as follows:

    curl --location --data "dims=[6,6]&bots=[crosscram.samples.windowshade-rand,crosscram.samples.random]" http://localhost:9999/game
    
The same game resource can also be created with this:

    curl --location --data "rows=6&cols=6&bot1=crosscram.samples.windowshade-rand&bot2=crosscram.samples.random" http://localhost:9999/game
    
Here the server is running locally on port 9999 and the game will be on a 6-by-6 board between the windowshade-rand and random sample bots.

## Viewing a Game

Send an HTTP GET request to a game resource URI.  If the request is successful, an HTML representation of the game is returned (see the note about media types below), otherwise, a 404 response is returned.

As an example, a browser or curl can be used to perform a GET on a URI such as:

    http://localhost:9999/game/123456

(assuming 123456 is a valid game resource).

### Game Resource Media Types

Currently a game resource can be represented by the following media types:

* text/html
* text/plain
* application/json

## View a List of All Played Games

Send an HTTP GET request to the /games resource.

### Games Resource Media Types

Currently the games resource can be represented by the following media types:

* text/html

## A Note About Media Types

The desired media type can be specified using the Accept HTTP header.  Alternatively, the Crosscram web service currently supports using an extension on the URL to specify the medial type.  For example, expanding upon the above example if one wants to obtain a JSON representation of game '123456', a GET request can be made to the following URL:

    http://localhost:9999/game/123456.json
	
If no extension is specified and no media types have been specified using the Accept header, the default is an HTML representation.  If a media type that is not supported is specified, then status code 415 "Unsupported Media Type" is returned.
