function prikaziUsera(e) {
    document.getElementById('callsDiv').style.display = 'none';
    document.getElementById('usersDiv').style.display = 'block';
    localStorage.setItem('prikaz', 'user');
}

function prikaziCalls(e) {
    document.getElementById('callsDiv').style.display = 'block';
    document.getElementById('usersDiv').style.display = 'none';
    localStorage.setItem('prikaz', 'call');
}

window.onload = function () {
    if (localStorage.getItem('prikaz') === 'call')
        prikaziCalls();
    else
        prikaziUsera();
};