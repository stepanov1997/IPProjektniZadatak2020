function prikaziUsera(e) {
    document.getElementById('callsDiv').style.display = 'none';
    document.getElementById('usersDiv').style.display = 'block';
    document.getElementById('dangerDiv').style.display = 'none';
    localStorage.setItem('prikaz', 'user');
}

function prikaziCalls(e) {
    document.getElementById('callsDiv').style.display = 'block';
    document.getElementById('usersDiv').style.display = 'none';
    document.getElementById('dangerDiv').style.display = 'none';
    localStorage.setItem('prikaz', 'call');
}

function prikaziCodebook() {
    document.getElementById('callsDiv').style.display = 'none';
    document.getElementById('usersDiv').style.display = 'none';
    document.getElementById('dangerDiv').style.display = 'block';
    localStorage.setItem('prikaz', 'danger');
}

window.onload = function () {
    const res = localStorage.getItem('prikaz');
    if (res === 'call')
        prikaziCalls();
    else if (res ==='danger'){
        prikaziCodebook();
    }
    else prikaziUsera();
};