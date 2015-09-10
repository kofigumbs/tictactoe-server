function getNewGameUri() {
    return "new?" +
        'size=' + $('[name=size]').val() + '&' +
        'vs=' + $('[name=vs]').val();
}

function makeSlot(slot, n) {
    if (slot == '-')
        return '<button data-position="' +
            n + '">' + n + '</button>';
    else
        return '<button disabled>' + slot + '</button>';
}

function makeSlots(board, size) {
    var html = '';
    for (var i = 0; i < size; i++) {
        for (var j = 0; j < size; j++) {
            var n = (i*size + j);
            html += makeSlot(board[n], n);
        }
        html += '<br>';
    }
    return html;
}

function move() {
    var uri = 'move?' +
        'position=' + $(this).data('position') +
        '&player-id=' + $('[data-player-id]').val();
    requestBoard(uri);
}

function getPlayerId(json) {
    var current = $('[data-player-id]').val();
    return current ? current : current = json['player-id'];
}

function makeBoard(json) {
    var board = json['board'];
    var size = Math.sqrt(board.length);
    var playerId = getPlayerId(json);
    $('[data-game]').html(makeSlots(board, size));
    $('[data-position]').on('click', move);
    var hidden = '<input type="hidden" value="' + playerId +
        '" data-player-id></input>';
    $('[data-game]').append(hidden);
    $('[data-game]').append(json['status']);
}

function requestBoard(uri) { $.getJSON(uri, makeBoard); }
function makeNewGame() { requestBoard(getNewGameUri()); }
$('[name=new]').on('click', makeNewGame);
