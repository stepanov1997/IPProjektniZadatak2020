function initMap(location = "", id = "", isCreate = true) {
    let lat = 44.76638889;
    let lng = 17.1866667;
    if(location.split(' ').length === 2)
    {
        lat = parseFloat(location.split(' ')[0]);
        lng = parseFloat(location.split(' ')[1]);
    }
    mapboxgl.accessToken = 'pk.eyJ1Ijoia2lraWtpa2kxOTkyIiwiYSI6ImNrOHoza2ZqejBhbGQzZGxjeGIxNWM0YnoifQ.3FoukhI7DUYFqV4W63mi6w';
    var map = new mapboxgl.Map({
        container: 'map'+id, // container id
        style: 'mapbox://styles/mapbox/streets-v11',
        center: [lng, lat], // starting position
        zoom: 15 // starting zoom
    });
    var marker = new mapboxgl.Marker({
        draggable: isCreate
    })
        .setLngLat([lng, lat])
        .addTo(map);

    if(isCreate)
    {
        function onDragEnd() {
            var lngLat = marker.getLngLat();
            document.getElementById('lat').value = lngLat.lat;
            document.getElementById('lng').value = lngLat.lng;
        }

        marker.on('drag', onDragEnd);
    }

    // Add zoom and rotation controls to the map.
    map.addControl(new mapboxgl.NavigationControl());
}