function sendGAEvent(category, action, label, isAgent) {
    if (isAgent === true || isAgent === "true") {
        ga('send', 'event', 'agent_' + category, action, label);
    } else {
        ga('send', 'event', category, action, label);
    }
}

$(document).ready($(function () {

    $('[data-metrics]').each(function () {
        var metrics = $(this).attr('data-metrics');
        var splitOnUser = metrics.split(';');
        var parts = splitOnUser[0].split(':');
        sendGAEvent(parts[0], parts[1], parts[2], splitOnUser[1]);
    });

}));
