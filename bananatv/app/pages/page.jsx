import React from 'react'
import '@/public/assets/css/LandPage.css'

const page = () => {
  return (
    <div className='landPage'>
      <h1>Bienvenido a BananaTV</h1>
      <img src="/img/logo.jpg" alt="" />
      <h3>Plataforma de Streaming de Series Y Películas</h3>
      <p>BananaTV se encuentra en fase de desarrollo BETA, por lo que algunas funciones no están disponilbes en este momento.</p>
      <p>Pedimos disculpas por los incovenientes.</p>
      <p>El equipo de desarrollo de BananaTV</p>
      <p>Mediza Sebastián, Pereyra Federico, Gerbruers Blas</p>
    </div>
  )
}

export default page