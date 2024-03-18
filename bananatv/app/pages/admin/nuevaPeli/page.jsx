'use client'
import '../../../../public/assets/css/Admin.css';

import React, { useState } from 'react';
import { useRouter } from 'next/navigation';

const page = () => {
    const router = useRouter();
    const [titulo, settitulo] = useState('');
    const [descripcion, setdescripcion] = useState('');
    const [fecha_lanzamiento, setfecha_lanzamiento] = useState('');
    const [duracion, setduracion] = useState('');
    const [productor, setproductor] = useState('');
    const [director, setDirector] = useState('');
    const [genero, setgenero] = useState('');
    const [urlPelicula, seturlPelicula] = useState('');
    const [banner, setBanner] = useState('');
    const [imagen, setImagen] = useState('');

    const handleSubmit = async (e) => {
        const api = 'http://localhost:7071/peliculas'
        e.preventDefault();
        const movie = { titulo, descripcion, fecha_lanzamiento, duracion, productor, director, genero, urlPelicula, banner, imagen };

        const res = await fetch(api, {
            method: 'POST',
            headers: { "Content-Type": "application/json", 'x-access-token': sessionStorage.getItem('x-access-token') },
            body: JSON.stringify(movie)
        })
        if (res.status === 200) {
            alert('Pelicula agregada con exito');
            router.push('/pages/admin');
        }

    };

    return (
        <div className='contenedor'>
            <div className='formContainer'>
                <form onSubmit={handleSubmit}>
                    <div className='titulo'>
                        <label>
                            Titulo:
                            <input type="text" value={titulo} onChange={(e) => settitulo(e.target.value)} />
                        </label>
                    </div>
                    <div className='desc'>
                        <label>
                            Descripcion:
                            <input value={descripcion} onChange={(e) => setdescripcion(e.target.value)} />
                        </label>
                    </div>
                    <div className='metadata'>
                        <label>
                            Release Date:
                            <input type="date" value={fecha_lanzamiento} onChange={(e) => setfecha_lanzamiento(e.target.value)} />
                        </label>
                        <label>
                            duracion:
                            <input type="text" value={duracion} onChange={(e) => setduracion(e.target.value)} />
                        </label>
                        <label>
                            Producer:
                            <input type="text" value={productor} onChange={(e) => setproductor(e.target.value)} />
                        </label>
                        <label>
                            Director:
                            <input type="text" value={director} onChange={(e) => setDirector(e.target.value)} />
                        </label>
                        <label>
                            genero:
                            <input type="text" value={genero} onChange={(e) => setgenero(e.target.value)} />
                        </label>
                    </div>
                    <div className='publi'>
                        <label>
                            Movie URL:
                            <input type="text" value={urlPelicula} onChange={(e) => seturlPelicula(e.target.value)} />
                        </label>
                        Banner:
                        
                        <label>
                            Imagen:
                            <input type="text" value={imagen} onChange={(e) => setImagen(e.target.value)} />
                        </label>
                    </div>
                    <div className='button mx-auto'>
                        <button className='btn btn-warning' type="submit">Submit</button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default page