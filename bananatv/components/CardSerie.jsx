import Link from 'next/link'

const CardSerie = ({producciones, editSerie, deleteSerie}) => {
    return (
        <div className="cardContainer">
            {producciones.map((item) => (
                <div className="card" key={item.idSerie}>
                    <Link href={`/pages/series/${item.idSerie}`}>
                        <img src={item.banner} className="card-img-top" alt="..." />
                    </Link>
                    <div className="card-body">
                        <h5 className="card-title">{item.titulo}</h5>
                        <button data-toggle="collapse" data-target="#dangerZone" className='btn btn-danger btn-lg'>Danger Zone</button>
                        <div className='collapse dangerZone' id='dangerZone'>
                            <Link href={`/pages/series/editar/${item.idSerie}`}>
                                <button className='btn btn-warning' onClick={() => editSerie(item)}>Editar Contenido</button>
                            </Link>
                            <button className='btn btn-danger' onClick={() => deleteSerie(item.idSerie)}>Borrar</button>
                        </div>
                    </div>
                </div>
            ))}
        </div>
    );
}

export default CardSerie