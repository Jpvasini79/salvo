$.ajax({
        url: "api/leaderboard"
    })
    .done(function (salvojson) {
        // json = [ { "name": ..., ... }, { "name": ..., ... }, ...]
        createLeaderboard(salvojson);
        formLogin(); //TODO piensa cuando se debería ejecutar esta función

        //console.log( salvojson );
    })
    .fail(function (jqxhr, textStatus, error) {
        var err = textStatus + ", " + error;
        console.log("Request Failed: " + err);
    });

$.ajax({
        url: "api/games"
    })
    .done(function (salvojson) {
        // json = [ { "name": ..., ... }, { "name": ..., ... }, ...]

        currentPlayer(salvojson);


        //console.log( salvojson );
    })
    .fail(function (jqxhr, textStatus, error) {
        var err = textStatus + ", " + error;
        console.log("Request Failed: " + err);
    });






$("#logclick").click(function (evt) {
    login(evt)
})

$(".cancelbtn").click(function (evt) {
    logout(evt)
})

$(".signupbutton").click(function (evt) {
    signIn(evt)
})


function currentPlayer(salvojson) {



    if (salvojson.currentPlayer == null) {

        var noCurrentP = $('<span class="currntp">' + "Current Player : " + "No player connected" + '</span>')
        $(".currentplayer").append(noCurrentP);

    } else {

        var currentP = $('<span class="currntp">' + "Current Player: " + salvojson.currentPlayer.username + '</span>')

        $(".currentplayer").append(currentP);

        $('.containerUserPwd').css("display", "none");

        var listElement = $('<ul class="mainlist"/>');


        $.each(salvojson.games, function (key, value) {


            var gamesId = value.gamePlayers;

            var p1 = value.gamePlayers[0].player.email;
            if (value.gamePlayers.length == 1) {
                var p2 = "No player joined";
            } else {
                var p2 = value.gamePlayers[1].player.email;
                var playerID2 = value.gamePlayers[1].player.id;
            }



            var gameIdNum = value.id;
            var playerID = value.gamePlayers[0].player.id;

            var creationDate = value.date;


            var liElementsGameId = $('<li class="list-group-item">' + " Game Id: " + gameIdNum + ";" + " Creation Date: " + creationDate + "</li>");
            var ulElementFirstPlayer = $('<ul class="onlyopenlist"/>');
            var liElementPlayerId = $('<li class="playerId">' + " Player ID: " + playerID + "</li>");
            var liElementPlayerId2 = $('<li class="playerId2">' + " Player ID: " + playerID2 + "</li>");
            var liElementPlayerName = $('<li class="playerName">' + " Player Email: " + p1 + "</li>");
            var liElementPlayerName2 = $('<li class="playerName2">' + " Player Email: " + p2 + "</li>");
            var ulElementSecondPlayer = $('<ul class="secondopenlist"/>');


            var buttonPlay = $('<button class="buttonplay" type="button">' + "Play!" + '</button>');
            var buttonJoin = $('<button class="buttonjoin" type="button">' + "Join!" + '</button>');






            $(".buttonlogin").click(function () {

            });

            listElement
                .append(liElementsGameId)

            var playB = false;

            $.each(gamesId, function (key2, value2) {



                if ((salvojson.currentPlayer.username == p1 || salvojson.currentPlayer.username == p2) && playB == false) {

                    playB = true;


                    var playButton = $('<td/>', {
                        text: 'PLAY',
                        id: 'play_game' + salvojson.currentPlayer.id,
                        class: 'buttonCells btn btn-warning btn-sm',
                        // style: 'display: flex; justify-content: center;',
                        click: function () {
                            window.location.replace("/gameship.html?gp=" + value2.gpid);
                        }

                    });

                    listElement.append(playButton);

                } else if (value.gamePlayers.length == 1) {

                    var joinButton = $('<td/>', {
                        text: 'JOIN',
                        id: 'join_game' + salvojson.currentPlayer.id,
                        class: 'buttonCells btn btn-warning btn-sm',
                        // style: 'display: flex; justify-content: center;',
                        click: function () {

                            $.post("/api/games/" + value.id + "/players", {


                                })


                                .done(
                                    function (response) {

                                        window.location.replace("/gameship.html?gp=" + response.gpid);


                                    })
                                .fail();
                        }
                    });

                    listElement.append(joinButton);


                }
            });


            liElementsGameId
                .append(ulElementFirstPlayer)
                .append(ulElementSecondPlayer);

            ulElementFirstPlayer
                .append(liElementPlayerId)
                .append(liElementPlayerName);

            ulElementSecondPlayer
                .append(liElementPlayerId2)
                .append(liElementPlayerName2);

        });

        $(".container").append(listElement);


        var createGButton = $('<td/>', {
            text: 'CREATE GAME',
            //id: 'create_game' + salvojson.currentPlayer.id,
            class: 'buttonCells btn btn-warning btn-sm',
            style: 'margin: auto; margin-top: 40px; margin-left: 46.5%',
            click: function () {

                $.post("/api/games", {
                        //name: form["name"].value,
                        //pwd: form["pwd"].value
                    })

                    .done(
                        function () {
                            console.log("You are logged in!")
                            location.reload();


                        })
                    .fail();
            }

        });
        $('.containerButton').append(createGButton);



    };


    //var currntPl = $('<span class="currntp">Current Player:</span>')

}

/*
function createGameButton() {
    
    var createGButton = $('<td/>', {
        text: 'CREATE GAME',
        //id: 'create_game' + salvojson.currentPlayer.id,
        class: 'buttonCells btn btn-warning btn-sm',
        // style: 'display: flex; justify-content: center;',
        click: function () {
            window.location.replace("http://localhost:8080/gameship.html?gp=" + salvojson.games["0"].gamePlayers["0"].gpid)
        }

    });
    $('.containerButton').append(createGButton);
}
*/

function login(evt) {
    evt.preventDefault();
    var form = evt.target.form

    $.post("/api/login", {
            name: form["name"].value,
            pwd: form["pwd"].value
        })

        .done(
            function () {
                console.log("You are logged in!")
                location.reload();


            })
        .fail();
}

function logout(evt) {
    evt.preventDefault();
    $.post("api/logout")

        .done(
            function () {

                console.log("You are logged out!")
                location.reload();
                document.location.href = 'http://localhost:8080/games.html'

            })
        .fail();
}

function signIn(evt) {
    
    evt.preventDefault();
    var form = evt.target.form

    $.post("/api/player", {
            name: form["name"].value,
            pwd: form["pwd"].value
        })

        .done(
            function () {

                console.log("You are logged out!")
                location.reload()
            })
        .fail(
            function () {
                console.log("Failed trying to sign in!")
            });

}

function formLogin() {

    $('.signin').click(function () {
        $('.formlogin').fadeOut(-100)

        $('.formnewlogin').fadeIn(-100)

        $('.fadeform').toggle();

    });

    $('.loginbotton').click(function () {
        $('.formlogin').fadeIn(-100)

        $('.formnewlogin').fadeOut(-100)

        $('.fadeform').toggle();

    });


}

function createLeaderboard(salvojson) {

    
    var tableElement = $('<table class="table table-bordered table-hover"/>');

    var theadElement = $('<thead/>');

    var tbodyElement = $('<tbody/>');

    var trowsElement = $('<tr class="rowshead"/>');


    $(".tableleaderboard").append(tableElement);



    var headerName = $('<th/>').html("Player");
    var headerScore = $('<th/>').html("Score");
    var headerWin = $('<th/>').html("Wins");
    var headerLoose = $('<th/>').html("Looses");
    var headerDraw = $('<th/>').html("Draws");

    trowsElement
        .append(headerName)
        .append(headerScore)
        .append(headerWin)
        .append(headerLoose)
        .append(headerDraw);

    theadElement
        .append(trowsElement);

    var firstObject = salvojson.leaderboard;


    for (var i = 0; i < firstObject.length; i++) {




        var trElementName = $('<tr class="rows"/>');


        var printName = $('<td class="lalal"/>').html(firstObject[i].name);
        var printScore = $('<td class="test2"/>').html(firstObject[i].score);
        var printWins = $('<td class="test2"/>').html(firstObject[i].wins);
        var printLoose = $('<td class="test2"/>').html(firstObject[i].lose);
        var printDraws = $('<td class="test2"/>').html(firstObject[i].draw);


        tbodyElement
            .append(trElementName);


        trElementName
            .append(printName)
            .append(printScore)
            .append(printWins)
            .append(printLoose)
            .append(printDraws);


        tableElement
            .append(theadElement)
            .append(tbodyElement);


        /* for (var j = 0; j < secondObject.length; j++) {
             
             

             console.log(secondObject[j].player.email);
             
             //jsonS.["secondObject[j].player.email"]= 0;

             

         }*/


    }


}

function getParameterByName() {
    var search = window.location.href;
    var obj = {};
    var reg = /(?:[?&]([^?&#=]+)(?:=([^&#]*))?)(?:#.*)?/g;

    search.replace(reg, function (match, param, val) {
        obj[decodeURIComponent(param)] = val === undefined ? "" : decodeURIComponent(val);
    });
    return obj.gp;
}


/*function createListGame(salvojson) {

    console.log(salvojson);

    var listElement = $('<ul/>');


    for (var i = 0; i < salvojson.games.length; i++) {

        var p1 = salvojson.games[i].gamePlayers[0].player.email;
        var p2 = salvojson.games[i].gamePlayers[1].player.email;

        console.log(p1);
        console.log(p2);

        var gameIdNum = salvojson.games[i].id;
        var playerID = salvojson.games[i].gamePlayers[0].player.id;
        var playerID2 = salvojson.games[i].gamePlayers[1].player.id;
        var creationDate = salvojson.games[i].date;


        var liElementsGameId = $('<li class="list-group-item">' + " Game Id: " + gameIdNum + ";" + " Creation Date: " + creationDate + "</li>");
        var ulElementFirstPlayer = $('<ul class="onlyopenlist"/>');
        var liElementPlayerId = $('<li class="playerId">' + " Player ID: " + playerID + "</li>");
        var liElementPlayerId2 = $('<li class="playerId2">' + " Player ID: " + playerID2 + "</li>");
        var liElementPlayerName = $('<li class="playerName">' + " Player Email: " + p1 + "</li>");
        var liElementPlayerName2 = $('<li class="playerName2">' + " Player Email: " + p2 + "</li>");
        var ulElementSecondPlayer = $('<ul class="secondopenlist"/>');





        listElement
            .append(liElementsGameId);

        liElementsGameId
            .append(ulElementFirstPlayer)
            .append(ulElementSecondPlayer);

        ulElementFirstPlayer
            .append(liElementPlayerId)
            .append(liElementPlayerName);

        ulElementSecondPlayer
            .append(liElementPlayerId2)
            .append(liElementPlayerName2);

    }

    $(".container").append(listElement);


}*/
