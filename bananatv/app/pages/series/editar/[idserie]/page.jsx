'use client'
import EditForm from '@/components/EditFormSerie'
import { useEffect, useState } from "react"

const page = ({ params }) => {
    const getPeli = async () => {
        const api = 'http://localhost:7071/series'
        const res = await fetch(`${api}/${params.idserie}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json', 'x-access-token': sessionStorage.getItem('x-access-token')
            }
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

    return (
        <EditForm data={data}/>
    )
}

export default page