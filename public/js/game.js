
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

var drawGame = function(game) {
    var canvas = document.getElementById('game-canvas');
    var context = canvas.getContext('2d');

    var playerColors = ['red', 'green'];

    //canvas.style.backgroundColor = 'red';

    var boxSizePix = 60;
    var numRows = game['dims'][0];
    var numCols = game['dims'][1];
    var width = numCols * boxSizePix;
    var height = numRows * boxSizePix;

    // set the canvas size
    canvas.width = width;
    canvas.height = height;
    
    // for each move in the history
    numMoves = game.history.length;
    var dominoes = [];
    for (move = 0; move < numMoves; ++move) {
	var coords = game.history[move];

	// create the domino
	dominoes[move] = new Domino(coords, boxSizePix, playerColors[move % 2]);
    }

    // loop over each domino and draw it
    for (d = 0; d < numMoves; ++d) {
	dominoes[d].draw(context);
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
	    // var pre = document.createElement('pre');
	    // pre.textContent = xhr.responseText;
	    // document.body.appendChild(pre);

	    var gameJSON = JSON.parse(xhr.responseText);
	    drawGame(gameJSON);
	}
    }
    xhr.open('GET', url, true);
    xhr.send();
}