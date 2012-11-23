function Domino(indices, shortSide, color) {
    this.shortSide = shortSide;
    this.color = color;

    // calc the dimensions of the rectangle to draw
    var coord1 = indices[0];
    var coord2 = indices[1];
    this.x = Math.min(coord1[1], coord2[1]) * shortSide;
    this.y = Math.min(coord1[0], coord2[0]) * shortSide;
    this.width = (Math.abs(coord1[1] - coord2[1]) + 1) * shortSide;
    this.height = (Math.abs(coord1[0] - coord2[0]) + 1) * shortSide;

    // draw function
    this.draw = function(ctx) {
	ctx.fillStyle = this.color;
	ctx.fillRect(this.x, this.y, this.width, this.height);

	// then, draw a black border
	ctx.lineWidth = 4;
	ctx.strokeStyle = 'black';
	ctx.strokeRect(this.x, this.y, this.width, this.height);
    }
}

function CrosscramGame(gameMap) {

    var boxSizePix = 60;
    this.rows = gameMap.dims[0];
    this.cols = gameMap.dims[1];
    
    // get the canvas and set its size
    var canvas = document.getElementById('game-canvas');
    var width = this.rows * boxSizePix;
    var height = this.cols * boxSizePix;
    canvas.width = width;
    canvas.height = height;

    // save a reference to the graphics context
    this.ctx = canvas.getContext('2d');

    // colors
    var playerColors = ['red', 'green'];

    // color the bot text in the list to give context
    var bot1_li = document.getElementById('bot1');
    var bot2_li = document.getElementById('bot2');
    bot1_li.style.color = playerColors[0];
    bot2_li.style.color = playerColors[1];

    // set up the dominoes
    var numMoves = gameMap.history.length;
    this.dominoes = [];
    for (move = 0; move < numMoves; ++move) {
	var coords = gameMap.history[move];

	// create the domino
	this.dominoes[move] = new Domino(coords, boxSizePix, playerColors[move % 2]);
    }

    // Draw the gameboard to the canvas. Give an integer as the number of moves
    // to draw.
    this.draw = function(step) {
	// clear the canvas
	this.ctx.clearRect(0, 0, this.ctx.width, this.ctx.height);

	// if called with zero args, draw all dominoes
	if (step == undefined) {
	    step = this.dominoes.length - 1;
	}

	for (d = 0; d < step + 1; ++d) {
	    this.dominoes[d].draw(this.ctx);
	}
    }
}

window.onload = function() {

    // get the 'self' link
    var links = document.getElementsByTagName('link');
    var selfLink;
    for (var i = 0; i < links.length; ++i) {
	var rel = links[i].rel;
	if (rel === 'self') {
	    selfLink = links[i];
	    break;
	}
    }

    // create the URL for a JSON representation
    var url = selfLink.href + '.json';

    xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function() {
	if (xhr.readyState == 4 && xhr.status == 200) {

	    var gameJSON = JSON.parse(xhr.responseText);
	    var numMoves = gameJSON.history.length;
	    var game = new CrosscramGame(gameJSON);
	    game.draw();

	    // center the canvas
	    var canvas = document.getElementById('game-canvas');
	    var gamediv = document.getElementById('game');
	    gamediv.style.width = canvas.width;
	}
    }
    xhr.open('GET', url, true);
    xhr.send();
}