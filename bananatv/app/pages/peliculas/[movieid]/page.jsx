'use client'

import { useState, useEffect } from "react"
import '../../../../public/assets/css/Movie.css'
import Info from "@/components/Info"

const page = ({ params }) => {
    const getPeli = async () => {
        const api = 'http://localhost:7071/peliculas'
        const res = await fetch(`${api}/${params.movieid}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json' , 'x-access-token' : sessionStorage.getItem('x-access-token')
            }
        });
        const data = await res.json();
        console.log(data);
        console.log(JSON.stringify(data.genero));
        setData(data);
        setLoading(false);
    }

    useEffect(() => {
        getPeli();
    }, []);

    const [data, setData] = useState([]); //useState([]) es el estado inicial, que es un array vacio
    const [loading, setLoading] = useState(true); //useState(false) es el estado inicial, que es un booleano en false
    const [error, setError] = useState(null); //useState(null) es el estado inicial, que es un null

    return (
        <>
            {loading ? 
                (<p>Cargando...</p>) : (<Info produccion={data} />)}
        </>
    )
}

export default page