'use client';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../public/assets/css/Nav.css';
import { useRouter } from 'next/navigation';


const Nav = () => {
    const router = useRouter();
    const handleCerrarCerrarSesion = async (e) => {
        e.preventDefault();
        sessionStorage.removeItem('x-access-token');
        router.push('/');
    }

    return (
        <nav className="navbar navbar-expand-lg">
            <div className="container-fluid">
                <a className="navbar-brand" href="/pages">
                    <img src="/img/logoSinFondo.png" alt="" />
                </a>
                <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                        <li className="nav-item">
                            <button type="button" className="btn btn-warning" data-toggle="modal" data-target="#categorias">
                                Categorias
                            </button>
                        </li>
                        <li className="nav-item">
                            <a className="btn btn-warning" aria-current="page" href="/pages/peliculas">Peliculas</a>
                        </li>
                        <li className="nav-item">
                            <a className="btn btn-warning" aria-current="page" href="/pages/series">Series</a>
                        </li>
                        <li className="nav-item">
                            <a className="btn btn-warning" aria-current="page" href="/pages/livetv">En Vivo</a>
                        </li>
                        <li className="nav-item">
                            <a className="btn btn-warning" aria-current="page" href='/pages/admin'>Agreagar Contenido</a>
                        </li>
                    </ul>
                </div>
                <form className="d-flex" role="search">
                    <input className="form-control me-2" type="search" placeholder="Search" aria-label="Search" />
                    <button className="btn btn-warning" type="submit">Buscar</button>
                </form>
                <img src="/img/man.png" alt="" className="btnImgAuth" data-toggle="modal" data-target="#miVentanaModal" />
                <div className="modal fade" id="miVentanaModal" tabIndex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                    <div className="modal-dialog" role="document">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title" id="exampleModalLabel">Título de la Ventana Modal</h5>
                                <button type="button" className="close" data-dismiss="modal" aria-label="Cerrar">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div className="modal-body">
                                <form onSubmit={handleCerrarCerrarSesion}>
                                    <button type='submit'>Cerrar Sesion</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="modal fade" id="categorias" tabIndex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                    <div className="modal-dialog" role="document">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title" id="exampleModalLabel">Título de la Ventana Modal</h5>
                                <button type="button" className="close" data-dismiss="modal" aria-label="Cerrar">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <div className="modal-body">
                                <p>Generos</p>
                                <table className='table-warning'>
                                    <tbody className='table table-bordered'>
                                        <tr>
                                            <td>Acción</td>
                                            <td>Aventura</td>
                                            <td>Comedia</td>
                                        </tr>
                                        <tr>
                                            <td>Documentales</td>
                                            <td>Drama</td>
                                            <td>Fantasía</td>
                                        </tr>
                                        <tr>
                                            <td>Terror</td>
                                            <td>Romance</td>
                                            <td>Ciencia ficción</td>
                                        </tr>
                                        <tr>
                                            <td>Animación</td>
                                            <td>Thriller</td>
                                            <td>Misterio</td>
                                        </tr>
                                        <tr>
                                            <td>Western</td>
                                            <td>Musical</td>
                                            <td>Historia</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div className="modal-footer">
                                <button type="button" className="btn btn-secondary" data-dismiss="modal">Cerrar</button>
                                <button type="button" className="btn btn-primary">Guardar cambios</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </nav>
    )
}

export default Nav