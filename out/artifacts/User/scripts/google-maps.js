// Initialize and add the map
var marker;
function initMap() {
    // The location of Uluru
    var uluru = {lat: 44.76638889, lng: 17.1866667};
    // The map, centered at Uluru
    var map = new google.maps.Map(
        document.getElementById('map'), {zoom: 4, center: uluru});
    // The marker, positioned at Uluru
    map.setZoom(17);
    marker = new google.maps.Marker({
        position: uluru,
        map: map,
        draggable: true
    });

    google.maps.event.addListener(
        marker,
        'drag',
        function (event) {
            document.getElementById('lat').value = this.position.lat();
            document.getElementById('lng').value = this.position.lng();
        });

    marker.addListener('click', function () {
        map.setZoom(8);
        map.setCenter(marker.getPosition());
    });

    google.maps.event.addListener(map, 'click', function (event) {
        marker.setPosition(event.latLng);
        document.getElementById('lat').value = event.latLng.lat();
        document.getElementById('lng').value = event.latLng.lng();
    });

    google.maps.event.addListener(marker, 'dragend', function (event) {
        document.getElementById('lat').value = this.position.lat();
        document.getElementById('lng').value = this.position.lng();
    });
}

google.maps.event.addDomListener(window, 'load', initMap);

function changePosition() {
    var lat = document.getElementById("lat").value;
    if(lat==="") return;
    var lng = document.getElementById("lng").value;
    if(lng==="") return;
    var latlng = new google.maps.LatLng(lat, lng);
    marker.setPosition(latlng);
}