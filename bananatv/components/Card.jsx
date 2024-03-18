import Link from 'next/link'

import '@/public/assets/css/Card.css'

const Card = ({ producciones, editPeli, deletePeli }) => {
    return (
        <div className="cardContainer">
            {producciones.map((item) => (
                <div className="card" key={item.idMovie}>
                    <Link href={`/pages/peliculas/${item.idMovie}`}>
                        <img src={item.banner} className="card-img-top" alt="..." />
                    </Link>
                    <div className="card-body">
                        <h5 className="card-title">{item.titulo}</h5>
                        <hr />
                        <button data-toggle="collapse" data-target="#dangerZone" className='btn btn-danger btn-lg'>Danger Zone</button>
                        <div className='collapse dangerZone' id='dangerZone'>
                            <Link href={`/pages/peliculas/editar/${item.idMovie}`}>
                                <button className='btn btn-warning' onClick={() => editPeli(item)}>Editar Contenido</button>
                            </Link>
                            <button className='btn btn-danger' onClick={() => deletePeli(item.idMovie)}>Borrar</button>
                        </div>
                    </div>
                </div>
            ))}
        </div>
    );
}

export default Card