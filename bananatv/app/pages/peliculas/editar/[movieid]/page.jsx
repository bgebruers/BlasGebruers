'use client'
import EditForm from '@/components/EditForm'
import { useEffect, useState } from "react"

const page = ({ params }) => {
    const getPeli = async () => {
        const api = 'http://localhost:7071/peliculas'
        const res = await fetch(`${api}/${params.movieid}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json', 'x-access-token': sessionStorage.getItem('x-access-token')
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

    return (
        <EditForm data={data}/>
    )
}

export default page