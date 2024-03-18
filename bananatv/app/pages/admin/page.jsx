import Link from 'next/link'
import React from 'react'

import '@/public/assets/css/AdminPage.css'

const page = () => {
  return (
    <div className='admin'>
        <h1>Hola Admin</h1>
        <p>Desde aqui podras agreagar nuevo contenido a la plataforma.</p>
        <p>Seleciona el tipo de contenido y completa el formulario con las datos apropiados</p>
        <p>Una vez completado el formulario, presiona el boton "Agregar" para agregar el contenido a la plataforma.</p>
        <div className='botones'>
            <Link href="/pages/admin/nuevaPeli">
                <button className='btn btn-warning'>Nueva Pelicula</button>
            </Link>
            <Link href="/pages/admin/nuevaSerie">
                <button className='btn btn-warning'>Nueva Serie</button>
            </Link>
        </div>
    </div>
  )
}

export default page