
const API_URL = "http://localhost:8080";

function entrar() {
    console.log("entrando...");
    listarAdmisiones();
    const formA = document.getElementById('frm');
    const modal = document.getElementById('modalEliminar');
    const btnM = document.getElementById('confirmarEliminar');
    formA.addEventListener('submit', function (e) {
        e.preventDefault();
        enviarFrmAdd();
    });
    modal.addEventListener('show.bs.modal', event => {
        const button = event.relatedTarget;
        const id = button.getAttribute('id');
        btnM.addEventListener('click', function(e) {
            e.preventDefault();
           confirmarEliminar(id); 
        });
    });
}

async function listarAdmisiones() {
    const tabla = document.querySelector("#tbl-admi");
    await fetch(`${API_URL}/api/admisiones`)
            .then((response) => response.json())
            .then((admisiones) => {
                console.log(admisiones);
                if (admisiones.length > 0) {
                    admisiones.forEach((dato) => {
                        let elemTr = document.createElement("tr");
                        let elemTd1 = document.createElement("td");
                        let elemTd2 = document.createElement("td");
                        elemTd1.innerHTML = `${dato.nombreAdmision}`;
                        elemTd2.innerHTML = `<button type="button" class="btn btn-primary btn-sm" data-bs-toggle="modal" 
                            data-bs-target="#exampleModal2" id="${dato.id}" op="Editar">Editar</button>
                        <a type="button" class="btn btn-danger btn-sm" data-bs-toggle="modal" 
                            data-bs-target="#modalEliminar" id="${dato.id}" op="Borrar">Borrar</a>`;
                        elemTr.appendChild((elemTd1));
                        elemTr.appendChild((elemTd2));
                        tabla.appendChild(elemTr);
                    });
                }
            });
}


async function enviarFrmAdd() {
    let idElement = document.getElementById('id');
    let id = idElement ? idElement.value : 0;
    let admi = document.getElementById('nombreAdmision').value;
    let act = document.getElementById('activo').checked;

    const params = {
        id: id,
        nombreAdmision: admi,
        activo: act
    };

    const options = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(params)
    };
    await fetch(`${API_URL}/api/guardar-admision`, options)
            .then((response) => {
                if (response.ok) {
                    alert(`${admi} guardado correctamente`);
                    //listarAdmisiones();
                    location.href = "/admisiones";
                } else {
                    console.log(response);
                    alert('Hubo un error, revisa los datos');
                }
            });

}
async function confirmarEliminar(id) {
    const modalEliminar = document.getElementById('modalEliminar');
    const btnConfirmarEliminar = document.getElementById('confirmarEliminar');

    const params = {
        id: id
    };
    const options = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(params)
    };
    await fetch(`${API_URL}/api/admisiones-borrar`, options)
        .then((response) => {
            if (response.ok) {
                alert(`Admisión eliminada correctamente`);
                //listarAdmisiones();
                location.href = "/admisiones";
            } else {
                console.log(response);
                alert('Hubo un error al eliminar la admisión');
            }
        });
    
}