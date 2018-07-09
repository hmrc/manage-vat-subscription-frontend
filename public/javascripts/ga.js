$(document).ready($(function () {

    $('[data-metrics]').each(function () {
        var metrics = $(this).attr('data-metrics');
        var splitOnUser = metrics.split(';');
        var parts = splitOnUser[0].split(':');
        if(splitOnUser[1] == "true"){
            console.log("ga('send', 'event', " + 'agent_' + parts[0] + ", " + parts[1] + ", " + parts[2] + ")");
        } else {
            console.log("ga('send', 'event'," + parts[0] + ", " + parts[1] + ", " + parts[2] + ")");
        }
    });

}));

function clickToGA(category, action, label, isAgent) {
    if (isAgent) {
        console.log("ga('send', 'event', " + 'agent_' + category + ", " + action + ", " + label + ")");
    } else {
        console.log("ga('send', 'event', " + category + ", " + action + ", " + label + ")");
    }
}
