'use client'
import { useRouter } from 'next/navigation'
import { useEffect, useState } from 'react'
import '@/public/assets/css/Form.css'

const EditForm = ({ data }) => {
    const router = useRouter()
    const api = 'http://localhost:7071/peliculas'
    const [newData, setNewData] = useState(data)

    useEffect(() => {
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
        const res = await fetch(`${api}/${data.idMovie}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'x-access-token': sessionStorage.getItem('x-access-token') },
            body: JSON.stringify(newData)
        });

        if (res.status === 200) {
            alert('Pelicula editada con exito')
            router.push('/pages/peliculas')
        }
    }

    return (
        <div>
            <form onSubmit={handleEdit}>
                <div>
                    <div className='inputs'>
                        <label htmlFor="id">
                            Id
                            <input type="text" name="id" value={newData.idMovie} onChange={handleInputChange} disabled />
                        </label>
                        <label htmlFor="titulo">
                            Titulo
                            <input type="text" name="titulo" value={newData.titulo} onChange={handleInputChange} />
                        </label>
                        <label htmlFor="descripcion">
                            Descripcion
                            <input type="textarea" name="descripcion" value={newData.descripcion} onChange={handleInputChange} />
                        </label>
                        <label htmlFor="fecha_lanzamiento">
                            Fecha de lanzamiento
                            <input type="date" name="fecha_lanzamiento" value={newData.fecha_lanzamiento} onChange={handleInputChange} />
                        </label>
                        <label htmlFor="duracion">
                            Duracion
                            <input type="number" name="duracion" value={newData.duracion} onChange={handleInputChange} />
                        </label>
                        <label htmlFor="productor">
                            Productor
                            <input type="text" name="productor" value={newData.productor} onChange={handleInputChange} />
                        </label>
                        <label htmlFor="director">
                            Director
                            <input type="text" name="director" value={newData.director} onChange={handleInputChange} />
                        </label>
                        <label htmlFor="genero">
                            Generos
                            <input type="text" name="genero" value={newData.genero} onChange={handleInputChange} />
                        </label>
                        <label htmlFor="urlPelicula">
                            Url de la pelicula
                            <input type="text" name='urlPelicula' value={newData.urlPelicula} onChange={handleInputChange} />
                        </label>
                    </div>
                    <div className='imgs'>
                        <div className='banner'>
                            <label htmlFor="">Banner</label>
                            <img src={newData.banner} alt="" onChange={handleInputChange} />
                            <input type="file" name='banner' />

                        </div>
                        <br />
                        <div className='imagen'>
                            <label htmlFor="">Imagen</label>
                            <img src={newData.imagen} alt="" onChange={handleInputChange} />
                            <input type="file" name='poster' />
                        </div>
                    </div>
                    <button type="submit">Submit</button>
                </div>
            </form>
        </div>
    )
}

export default EditForm