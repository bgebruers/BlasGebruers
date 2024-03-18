'use client'
import { useRouter } from 'next/navigation'
import { useEffect, useState } from 'react'

const EditForm = ({ data }) => {
    const router = useRouter()
    const api = 'http://localhost:7071/series'
    const [newData, setNewData] = useState(data)
    
    useEffect(() => {
        console.log('new data:' + newData);
        setNewData(data)
    }, [data])

    const handleInputChange = (event) => {
        const { name, value } = event.target;
        setNewData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };

    const handleEdit = async (e) => {
        e.preventDefault();
        const res = await fetch(`${api}/${newData.idSerie}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'x-access-token': sessionStorage.getItem('x-access-token') },
            body: JSON.stringify(newData)
        });

        if (res.status === 200) {
            alert('Serie editada con exito')
            router.push('/pages/series')
        }
    }

    return (
        <div>
            <form onSubmit={handleEdit}>
                <div>
                    <input type="text" name="idSerie" value={newData.idSerie} onChange={handleInputChange} disabled />
                    <input type="text" name="titulo" value={newData.titulo} onChange={handleInputChange} />
                    <input type="textarea" name="descripcion" value={newData.descripcion} onChange={handleInputChange} />
                    <input type="date" name='fecha_lanzamiento' value={newData.fecha_lanzamineto} onChange={handleInputChange} />
                    <input type="number" name='temporadas' value={newData.temporadas} onChange={handleInputChange} />
                    <input type="number" name='caputulos' value={newData.caputulos} onChange={handleInputChange} />
                    <input type="text" name='director' value={newData.director} onChange={handleInputChange} />
                    <input type="text" name='genero' value={newData.genero} onChange={handleInputChange} />
                    <input type="text" name='urlSerie' value={newData.urlPelicula} onChange={handleInputChange} />
                    <div>
                        <img src={newData.banner} alt="" onChange={handleInputChange} />
                        <input type="file" name='banner' />

                    </div>
                    <div>
                        <img src={newData.imagen} alt="" onChange={handleInputChange} />
                        <input type="file" name='poster' />
                    </div>
                    <button type="submit">Submit</button>
                </div>
            </form>
        </div>
    )
}

export default EditForm