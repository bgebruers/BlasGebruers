import '../public/assets/js/Info.js'
import '../public/assets/css/Movie.css'
import Link from 'next/link.js'

const Info = ({ produccion }) => {
    return (
        <div>
            {
                <div className="movie">
                    <div className="mx-auto text-center mainImgCont">
                        <img className="mainImg" src={produccion.imagen} alt="" />
                    </div>
                    <br />
                    <div className="movieData">
                        <div className="movieMeta">
                            <div className="title">
                                <h1>{produccion.titulo}</h1>
                                <svg xmlns="http://www.w3.org/2000/svg" height="2em" viewBox="0 0 512 512"><path d="M307 34.8c-11.5 5.1-19 16.6-19 29.2v64H176C78.8 128 0 206.8 0 304C0 417.3 81.5 467.9 100.2 478.1c2.5 1.4 5.3 1.9 8.1 1.9c10.9 0 19.7-8.9 19.7-19.7c0-7.5-4.3-14.4-9.8-19.5C108.8 431.9 96 414.4 96 384c0-53 43-96 96-96h96v64c0 12.6 7.4 24.1 19 29.2s25 3 34.4-5.4l160-144c6.7-6.1 10.6-14.7 10.6-23.8s-3.8-17.7-10.6-23.8l-160-144c-9.4-8.5-22.9-10.6-34.4-5.4z" /></svg>
                            </div>
                            <div>
                                <p>estrellas | puntuacion media | cant reseñas</p>
                            </div>
                            <div className="btnContainer">
                                <button className='btn btn-warning'>
                                    <svg xmlns="http://www.w3.org/2000/svg" height="2em" viewBox="0 0 512 512"><path d="M47.6 300.4L228.3 469.1c7.5 7 17.4 10.9 27.7 10.9s20.2-3.9 27.7-10.9L464.4 300.4c30.4-28.3 47.6-68 47.6-109.5v-5.8c0-69.9-50.5-129.5-119.4-141C347 36.5 300.6 51.4 268 84L256 96 244 84c-32.6-32.6-79-47.5-124.6-39.9C50.5 55.6 0 115.2 0 185.1v5.8c0 41.5 17.2 81.2 47.6 109.5z" /></svg>
                                    Añadir a Favoritos</button>
                                <button className='btn'>
                                    <svg xmlns="http://www.w3.org/2000/svg" height="2em" viewBox="0 0 448 512"><path d="M256 80c0-17.7-14.3-32-32-32s-32 14.3-32 32V224H48c-17.7 0-32 14.3-32 32s14.3 32 32 32H192V432c0 17.7 14.3 32 32 32s32-14.3 32-32V288H400c17.7 0 32-14.3 32-32s-14.3-32-32-32H256V80z" /></svg>
                                    Añadir Lista</button>
                            </div>
                            <br />
                            <div>{produccion.descripcion}</div>
                            <hr />
                            <div className='info'>
                                <button data-toggle="collapse" data-target="#masInfo" className='btn btn-outline-warning btn-sm'>Mas Info</button>
                            </div>
                            <div className='collapse' id='masInfo'>
                                <hr />
                                <p>Productor: {produccion.productor}</p>
                                <p>Generos: {produccion.genero}</p>
                                <p>Director: {produccion.director}</p>
                                <p>Estreno: {produccion.fecha_lanzamiento}</p>
                                <p>Duracion: {produccion.duracion} minutos</p>
                            </div>
                            <br />
                        </div>
                        <div className='play'>
                            <Link href={produccion.urlPelicula}>
                                <button className='btn btn-lg btn-warning'>Play</button>
                            </Link>
                        </div>
                    </div>
                </div>
            }
            <br />
        </div>
    )
}

export default Info