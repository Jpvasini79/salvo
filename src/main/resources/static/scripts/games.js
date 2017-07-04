$.ajax({
        url: "api/game_view/" + getParameterByName()
    })
    .done(function (salvojson) {

        //TODO las dos funciones son casi iguales, puedes y debes unificarlas



        createGrid(11, 11);
        createGridForTable2(11, 11);
        gridColorlocation(salvojson);
        createVsPlayers(salvojson);
        createTableShips(salvojson);
        displayNoneShips(salvojson);




        /*);*/

    })
    .fail(function (jqxhr, textStatus, error) {
        var err = textStatus + ", " + error;
        console.log("Request Failed: " + err);
    });


/*$("#testbutton").click(function () {
    shipPlacement()
})*/

var ships = [{

    shipName: "Carrier",
    locations: [],
    length: "5"
            }, {
    shipName: "Battleship",
    locations: [],
    length: "4"
            }, {
    shipName: "Submarine",
    locations: [],
    length: "3"
            }, {
    shipName: "Destroyer",
    locations: [],
    length: "3"
            }, {
    shipName: "Patrol Boat",
    locations: [],
    length: "2"

    }]


var salvoShotsLocations = [];
var canShoot = false;


var currentShip;

function createGrid(x, y) {

    var numbers = ["", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"]
    var letters = ["", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", ]

    var arrY = new Array(),
        arrX,
        container = $(".tablez");


    for (var iy = 0; iy < y; iy++) {
        arrX = new Array();


        for (var ix = 0; ix < x; ix++) {



            var idtext = letters[iy] + "" + numbers[ix];

            arrX[ix] = '<div class="cell" id="' + idtext + '">&nbsp;</div>'

            if (iy == 0) {


                arrX[ix] = '<div class="cell2">' + numbers[ix] + '</div>';

            }

            if (ix == 0) {

                arrX[ix] = '<div class="cell2">' + letters[iy] + '</div>'
            }


        }

        arrY[iy] = '<div class="row">' + arrX.join("\r\n") + '</div>';

    }

    container.append(arrY.join("\r\n"));

    $(".cell").click(function (evt) {

        //ships[currentShip].location.push($(this).attr('id'));

        //$(".cell").not(evt.target).removeClass("barco");

        //$(this).addClass("barco");

        shipsBoard($(this).attr('id'));
    })

}


function createGridForTable2(x, y) {

    var numbers = ["", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"]
    var letters = ["", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", ]
    var difference = ["S"];
    var arrY = new Array(),
        arrX,
        container2 = $(".tablez2");

    for (var iy = 0; iy < y; iy++) {
        arrX = new Array();


        for (var ix = 0; ix < x; ix++) {



            var idtext = letters[iy] + "" + numbers[ix] + "" + difference;

            arrX[ix] = '<div class="cell2table" id="' + idtext + '">&nbsp;</div>'

            if (iy == 0) {


                arrX[ix] = '<div class="cellnum">' + numbers[ix] + '</div>';



            }

            if (ix == 0) {

                arrX[ix] = '<div class="cellnum">' + letters[iy] + '</div>'
            }


        }

        arrY[iy] = '<div class="row2">' + arrX.join("\r\n") + '</div>';

    }

    container2.append(arrY.join("\r\n"));

    $('.cell2table').click(function () {

        salvoesShots($(this).attr('id'));
        //$(this).addClass('emptyshot');

    })

}

//TODO buscar alternativa para coger el valor de un parametro de la url por nombre

function getParameterByName() {
    var search = window.location.href;
    var obj = {};
    var reg = /(?:[?&]([^?&#=]+)(?:=([^&#]*))?)(?:#.*)?/g;

    search.replace(reg, function (match, param, val) {
        obj[decodeURIComponent(param)] = val === undefined ? "" : decodeURIComponent(val);
    });
    return obj.gp;
}

function gridColorlocation(salvojson) {

    var namesOponents = salvojson.gamePlayers


    var ships = salvojson.ships;

    var salvoes = salvojson.salvoes;



    for (var i = 0; i < ships.length; i++) {

        var shiplocation = ships[i].locations;



        for (var j = 0; j < shiplocation.length; j++) {

            shiplocationId = shiplocation[j];

            //$('#' + shiplocationId).css("background-color", "green");
            $('#' + shiplocationId).addClass("barco");
        }
    }

    for (var x = 0; x < salvoes.length; x++) {

        //Primer for para recorrer el primer array de objetos porque son 3.
        var shotslocation = salvoes[x]
        var p1 = namesOponents[x].gpid;




        for (var z = 0; z < shotslocation.length; z++) {

            var infolocation = salvoes[x][z]

            var playersID = salvoes[x][z].players;



            for (var y = 0; y < infolocation.location.length; y++) {

                var shotColorslocation = infolocation.location[y]

                //$('#' + shotColorslocation).css("background-color", "blue")
                //var onlyColors = '#' + shotColorslocation;

                if (p1 == getParameterByName()) {


                    $('#' + shotColorslocation).addClass("emptyshot").att

                } else {
                    $('#' + shotColorslocation + "S").addClass("emptyshot")
                }



                if ($('#' + shotColorslocation).hasClass("barco")) {

                    $('#' + shotColorslocation).removeClass("barco");
                    $('#' + shotColorslocation).removeClass("emptyshot");
                    $('#' + shotColorslocation).addClass("hitted");

                }
            }
        }
        /*for (var z = 0; z < shotslocation.length; z++) {
            
            //Segundo for para recorrer el segundo array de objetos.    
            var shotslocation2 = shotslocation[z][z]


            for (var y = 0; y < shotslocation2.length; y++) {
                
                //y el ultimo for para por fin recorrer las location de los shots.
                
                shotslocationId = shotslocation2[y]
                console.log(shotslocationId);

                $('#' + shotslocationId).css("background-color", "red")
                
                
            }
            
        }*/

    }


}

/*function shipPlacement() {






    $.post({

            url: "api/games/players/" + getParameterByName() + "/ships",

            data: JSON.stringify(ships),
            contentType: "application/json"
        })
        .done(function (response, status, jqXHR) {
            alert("Ship added: " + response);
        })
        .fail(function (jqXHR, status, httpError) {
            alert("Failed to add pet: " + textStatus + " " + httpError);
        })







}*/

function createVsPlayers(salvojson) {

    var namesOponents = salvojson.gamePlayers



    var p1 = namesOponents[0].player.email;
    var p2 = namesOponents[1].player.email;

    if (namesOponents[0].gpid == getParameterByName()) {


        var playerYou = $('<div class="playeryou"/>');
        var pText = $('<p>' + p1 + " (you)" + '</p>');
        var vsTextClass = $('<div class="vstext"/>');
        var vsText = $('<p>' + "VS" + '</p>');
        var playerEnemy = $('<div class="playerenemy"/>');
        var pText2 = $('<p>' + p2 + '</p>');


        playerYou
            .append(pText);

        vsTextClass
            .append(vsText);

        playerEnemy
            .append(pText2);

        $('.playersvs').append(playerYou);
        $('.playersvs').append(vsTextClass);
        $('.playersvs').append(playerEnemy);

    } else

    {

        var playerYou = $('<div class="playeryou"/>');
        var pText = $('<p>' + p1 + '</p>');
        var vsTextClass = $('<div class="vstext"/>');
        var vsText = $('<p>' + "VS" + '</p>');
        var playerEnemy = $('<div class="playerenemy"/>');
        var pText2 = $('<p>' + p2 + " (you)" + '</p>');


        playerYou
            .append(pText);

        vsTextClass
            .append(vsText);

        playerEnemy
            .append(pText2);


        $('.playersvs').append(playerEnemy);
        $('.playersvs').append(vsTextClass);
        $('.playersvs').append(playerYou);

    }

}

function createTableShips(salvojson) {

    if ($("#horizontal").is(':checked') || $("#vertical").is(':checked')) {

        $(".carrierspace").click(function (evt) {

            console.log($(this).attr('data-position'));
            currentShip = $(this).attr('data-position');
            //cada click que haga en una clase quitara la clase "shipselected" 
            $(".littleship").not(evt.target).removeClass("shipselected");

            $(this).addClass("shipselected");


        });

        $(".battleshipspace").click(function (evt) {
            console.log($(this).attr('data-position'));

            currentShip = $(this).attr('data-position');
            $(".littleship").not(evt.target).removeClass("shipselected")
            $(this).addClass("shipselected");

        });

        $(".submarinespace").click(function (evt) {
            console.log($(this).attr('data-position'));

            currentShip = $(this).attr('data-position');
            $(".littleship").not(evt.target).removeClass("shipselected")

            $(this).addClass("shipselected");

        });

        $(".destroyerspace").click(function (evt) {
            console.log($(this).attr('data-position'));

            currentShip = $(this).attr('data-position');
            $(".littleship").not(evt.target).removeClass("shipselected")

            $(this).addClass("shipselected");

        });

        $(".patrolbspace").click(function (evt) {
            console.log($(this).attr('data-position'));

            currentShip = $(this).attr('data-position');
            $(".littleship").not(evt.target).removeClass("shipselected")

            $(this).addClass("shipselected");

        });

    } else {

        alert("First select the ship position!")


    }

};

function shipsBoard(pickID) {

    $('.buttonflex').empty();

    if (document.getElementById("horizontal").checked) {
        //me sacas los barcos en horizontal


        $('.messageError').empty();



        var shipName = ships[currentShip].shipName
        var shipLocation = ships[currentShip].locations;
        var size = ships[currentShip].length;
        var letter = pickID.substring(0, 1);
        var num = Number(pickID.substring(1, 3));

        console.log(letter);
        console.log(num);




        for (var j = 0; j < shipLocation.length; j++) {

            $('#' + ships[currentShip].locations[j]).removeClass("barco");

        }

        ships[currentShip].locations.length = 0;

        for (var x = 0; x < size; x++) {

            var cellName = letter + (num + x);

            if (num + x > 10 || $('#' + cellName).hasClass("barco")) {

                var messageError = $('.messageError').text();

                if (!/Wrong position, ship doesn't fit!/.test(messageError)) {

                    $('.messageError').append("Wrong position, ship doesn't fit!");

                }

                return;
            }
        }

        for (var i = 0; i < size; i++) {

            var cellName = letter + (num + i);

            $('#' + cellName).addClass("barco");

            shipLocation.push(cellName);

        }

        console.log(shipLocation);



    } else if (document.getElementById("vertical").checked) {
        //me sacas los barcos en vertical


        $('.messageError').empty();

        var shipLocation = ships[currentShip].locations;
        var size = ships[currentShip].length;
        var letter = pickID.substring(0, 1);
        var num = Number(pickID.substring(1, 3));

        // el mismo array que tengo para crear el grid, pero ahora solo para en vertical
        var alphabet = ["", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"]


        var indexLetters = alphabet.indexOf(letter);

        for (var j = 0; j < shipLocation.length; j++) {

            $('#' + ships[currentShip].locations[j]).removeClass("barco");

        }
        ships[currentShip].locations.length = 0;


        for (var x = 0; x < size; x++) {
            console.log(indexLetters + x)

            var letterToRight = indexLetters + x;
            var cellName = alphabet[letterToRight] + num;

            if ((indexLetters + x) > 11 || $('#' + cellName).hasClass("barco")) {

                if (!/Wrong position, ship doesn't fit!/.test(messageError)) {

                    $('.messageError').append("Wrong position, ship doesn't fit!");

                }


                return;
            }
        }
        for (var i = 0; i < size; i++) {

            var letterToRight = indexLetters + i;
            var cellName = alphabet[letterToRight] + num;


            $('#' + cellName).addClass("barco");


            shipLocation.push(cellName);
            //TODO crear iuna funcion que me dectete los NaN 

        }

        console.log(shipLocation);

    }

    // se crea una variable llamada totalShips que me ayuda para que cuando sean 5 loacalizaciones
    // dentro de el json de ships para despues enviarlos al json.

    var totalShips = 0;


    for (var z = 0; z < ships.length; z++) {



        if (ships[z].locations.length > 0) {

            totalShips++

        }

        if (totalShips == 5) {
            var buttonSendShips = $('<button class="buttonsend" type="button">' + 'Send Ships!' + '</button>')
            var buttonFlex = $(".buttonflex");


            buttonFlex.append(buttonSendShips)

            $(".buttonsend").click(function () {

                $.post({
                        url: "api/games/players/" + getParameterByName() + "/ships",
                        data: JSON.stringify(ships),
                        dataType: "text",
                        contentType: "application/json"
                    })
                    .done(function () {
                        location.reload();
                    })
                    .fail(function (jqXHR, status, httpError) {
                        alert("Failed to add pet: " + textStatus + " " + httpError);
                    })

            })
        }
    }


    /*if(ships["0"].shipName == "Carrier" && ships["0"].location.length == 5){
        console.log("holis");
    }*/

}

function displayNoneShips(salvojson) {

    console.log(salvojson);
    for (var i = 0; i < salvojson.ships.length; i++) {

        if (salvojson.ships[i].locations.length > 1) {

            $(".tableplaceships").css("display", "none");
            canShoot = true;

        }

    }
}

function salvoesShots(pickID) {



    if (canShoot) {

        if (salvoShotsLocations.length < 5) {

            if (pickID.length == 4) {

                var letter = pickID.substring(0, 3);

            } else {

                var letter = pickID.substring(0, 2);
            }

            console.log(letter)

            $('#' + pickID).addClass('emptyshot');

            salvoShotsLocations.push(letter);





        } else {
            return;
        }

        var messageError = $('.messageError').text();

        if ($('#' + pickID).hasClass('emptyshot')) {

            if (!/Wrong position, ship doesn't fit!/.test(messageError)) {

                $('.messageError').append("Wrong position, ship doesn't fit!");

            }


            return;
        }

        if (salvoShotsLocations.length == 5) {

            var buttonShotsFired = $('<button class="shotsfired" type="button">' + 'Shot Salvoes!' + '</button>')
            var buttonFlex = $(".buttonflexshots");


            buttonFlex.append(buttonShotsFired);

            $(".buttonsend").click(function () {

                $.post({
                        url: "api/games/players/" + getParameterByName() + "/salvoes",
                        data: JSON.stringify(salvoes),
                        dataType: "text",
                        contentType: "application/json"
                    })
                    .done(function () {
                        location.reload();
                    })
                    .fail(function (jqXHR, status, httpError) {
                        alert("Failed to add pet: " + textStatus + " " + httpError);
                    })

            })

        }

    }
    console.log(salvoShotsLocations)
}
