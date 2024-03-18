'use client'

import { useState, useEffect } from "react"
import '../../../../public/assets/css/Movie.css'
import InfoSerie from "@/components/InfoSerie"

const page = ({ params }) => {
    const getPeli = async () => {
        const api = 'http://localhost:7071/series'
        const res = await fetch(`${api}/${params.idserie}`, {
            method: 'GET',
            headers: { 'Content-Type': 'application/json', 'x-access-token': sessionStorage.getItem('x-access-token') }
        });
        const data = await res.json();
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
            {loading ? (<p>Cargando...</p>) : (
                <InfoSerie produccion={data} />
            )}
        </>
    )
}

export default page