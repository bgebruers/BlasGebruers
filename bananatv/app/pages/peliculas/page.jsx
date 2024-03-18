'use client'
import { useEffect, useState } from "react"
import Card from "@/components/Card"

import '@/public/assets/css/Card.css'
import '@/public/assets/css/BarraBusqueda.css'

const page = () => {
    const api = 'http://localhost:7071/peliculas'

    const getPelis = async () => {
        const res = await fetch(api, {
            method: 'GET',
            headers: { 'Content-Type': 'application/json', 'x-access-token': sessionStorage.getItem('x-access-token') }

        });
        const data = await res.json();
        console.log(data);
        setData(data);
        setSearchData(data);
        setLoading(false);
    }

    const editPeli = async (idmovie) => {
        const res = await fetch(`${api}/${idmovie}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json', 'x-access-token': sessionStorage.getItem('x-access-token') }
        });
        const data = await res.json();
        console.log(data);
        getPelis();
    }

    const deletePeli = async (idmovie) => {
        const res = await fetch(`${api}/${idmovie}`, {
            method: 'DELETE',
            headers: { 'Content-Type': 'application/json', 'x-access-token': sessionStorage.getItem('x-access-token') }
        });
        const data = await res.json();
        console.log(data);
        getPelis();
    }

    useEffect(() => {
        getPelis();
    }, []);

    const [data, setData] = useState([]); //useState([]) es el estado inicial, que es un array vacio
    const [searchData, setSearchData] = useState([]); //useState([]) es el estado inicial, que es un array vacio
    const [searchTerm, setSearchTerm] = useState(""); //useState("") es el estado inicial, que es un string vacio
    const [loading, setLoading] = useState(true); //useState(false) es el estado inicial, que es un booleano en false
    const [error, setError] = useState(null); //useState(null) es el estado inicial, que es un null

    const handleChange = (e) => {
        setSearchTerm(e.target.value);
        filtrar(e.target.value);
    }

    const filtrar = (terminoBusqueda) => {
        var resultadosBusqueda = searchData.filter((elemento) => {
            if (elemento.titulo.toString().toLowerCase().includes(terminoBusqueda.toLowerCase())
            ) {
                return elemento;
            }
        });
        setData(resultadosBusqueda);
    }


    return (
        <>
            {loading ? (<p>Cargando...</p>) : (
                <div>
                    <div className="title">
                        <h1>Peliculas</h1>
                    </div>
                    <div className="idk">
                        <div className="busquedaContenedor">
                            <input
                                className="busqueda"
                                type="text"
                                value={searchTerm}
                                placeholder="Búsqueda por Nombre o Empresa"
                                onChange={handleChange} />
                        </div>
                    </div>
                    <br />
                    <Card producciones={data} deletePeli={deletePeli} editPeli={editPeli}/>
                </div>
            )}
        </>
    )
}

export default page