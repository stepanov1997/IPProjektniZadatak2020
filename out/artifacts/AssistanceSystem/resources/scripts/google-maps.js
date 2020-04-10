// Initialize and add the map
let marker;

function initMap(location, id) {
    // The location of Uluru
    const lat = location.toString().split(" ")[0];
    const lng = location.toString().split(" ")[1];
    const uluru = {lat: lat, lng: lng};
    // The map, centered at Uluru
    const map = new google.maps.Map(
        document.getElementById("map"+id), {zoom: 4, center: uluru});
    // The marker, positioned at Uluru
    map.setZoom(17);
    marker = new google.maps.Marker({
        position: uluru,
        map: map,
        draggable: true
    });
}