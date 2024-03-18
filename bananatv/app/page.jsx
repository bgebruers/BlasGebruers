'use client'
import '../public/assets/css/auth.css'
import { useState } from 'react'
import { useRouter } from 'next/navigation'

const page = () => {
  const router = useRouter()

  const [nombre, setNombre] = useState('');
  const [mail, setMail] = useState('');
  const [password, setPassword] = useState('');
  const [nroTarjeta, setnroTarjeta] = useState('');
  const [cardholderName, setCardholderName] = useState('');
  const [vencimiento, setvencimiento] = useState('');
  const [ccv, setccv] = useState('');
  const [tipoPlan, settipoPlan] = useState(1);
  const [isLoading, setIsLoading] = useState(false);

  const handleLogin = async (e) => {
    const api = 'http://localhost:7071/login';
    e.preventDefault();
    setIsLoading(true);
    const res = await fetch(api, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ mail, password })
    })

    const data = await res.json();
    console.log(data.data);

    if (res.status === 200) {
      sessionStorage.setItem('x-access-token', data.data.userData.token);
      sessionStorage.setItem('userName', data.data.userData.nombre);
      setIsLoading(false);
      router.refresh();
      router.push('/pages');
    }
  }

  const handleRegister = async (e) => {
    const api = 'http://localhost:7071/registro';
    e.preventDefault();
    setIsLoading(true);
    const res = await fetch(api, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ nombre, mail, password, tipoPlan, nroTarjeta, cardholderName, vencimiento, ccv })
    })

    const data = await res.json();
    console.log(data);

    if (res.status === 200) {
      alert('Usuario creado correctamente, ahora ya podes ingresar a tu cuenta');
      setIsLoading(false);
      router.refresh();
      router.push('/');
    }
  }

  return (
    <div className="container" id="container">
      <div className="form-container sign-up">
        <form onSubmit={handleRegister}>
          <input
            className='formInput'
            type="text"
            placeholder="Name"
            onChange={(e) => setNombre(e.target.value)}
            value={nombre} />
          <input
            type="email"
            placeholder="Email"
            onChange={(e) => setMail(e.target.value)}
            value={mail} />
          <input
            type="password"
            placeholder="Password"
            onChange={(e) => setPassword(e.target.value)}
            value={password} />
          <span>Plan</span>
          <select onChange={(e) => settipoPlan(e.target.value)}>
            <option value="1">Basic</option>
            <option value="2">Estandar</option>
            <option value="3">Premium</option>
          </select>
          <span>Payment Details</span>
          <input
            type="text"
            placeholder="Card Number"
            onChange={(e) => setnroTarjeta(e.target.value)}
            value={nroTarjeta}
          />

          <input
            type="text"
            placeholder="Cardholder Name"
            onChange={(e) => setCardholderName(e.target.value)}
            value={cardholderName}
          />

          <input
            type="text"
            placeholder="Expiration Date"
            onChange={(e) => setvencimiento(e.target.value)}
            value={vencimiento}
          />

          <input
            type="text"
            placeholder="ccv"
            onChange={(e) => setccv(e.target.value)}
            value={ccv}
          />
          <button type='submit'>Registrame</button>
        </form>
      </div>
      <div className="form-container sign-in">
        <form onSubmit={handleLogin}>
          <h1>Inicia Sesion</h1>
          <div className="social-icons">
            <a href="#" className="icon">
              <svg xmlns="http://www.w3.org/2000/svg" height="1em" viewBox="0 0 512 512">
                <path d="M504 256C504 119 393 8 256 8S8 119 8 256c0 123.78 90.69 226.38 209.25 245V327.69h-63V256h63v-54.64c0-62.15 37-96.48 93.67-96.48 27.14 0 55.52 4.84 55.52 4.84v61h-31.28c-30.8 0-40.41 19.12-40.41 38.73V256h68.78l-11 71.69h-57.78V501C413.31 482.38 504 379.78 504 256z" />
              </svg>
            </a>
            <a href="#" className="icon">
              <svg xmlns="http://www.w3.org/2000/svg" height="1em" viewBox="0 0 488 512">
                <path d="M488 261.8C488 403.3 391.1 504 248 504 110.8 504 0 393.2 0 256S110.8 8 248 8c66.8 0 123 24.5 166.3 64.9l-67.5 64.9C258.5 52.6 94.3 116.6 94.3 256c0 86.5 69.1 156.6 153.7 156.6 98.2 0 135-70.4 140.8-106.9H248v-85.3h236.1c2.3 12.7 3.9 24.9 3.9 41.4z" />
              </svg>
            </a>
            <a href="#" className="icon">
              <svg xmlns="http://www.w3.org/2000/svg" height="1em" viewBox="0 0 512 512">
                <path d="M459.37 151.716c.325 4.548.325 9.097.325 13.645 0 138.72-105.583 298.558-298.558 298.558-59.452 0-114.68-17.219-161.137-47.106 8.447.974 16.568 1.299 25.34 1.299 49.055 0 94.213-16.568 130.274-44.832-46.132-.975-84.792-31.188-98.112-72.772 6.498.974 12.995 1.624 19.818 1.624 9.421 0 18.843-1.3 27.614-3.573-48.081-9.747-84.143-51.98-84.143-102.985v-1.299c13.969 7.797 30.214 12.67 47.431 13.319-28.264-18.843-46.781-51.005-46.781-87.391 0-19.492 5.197-37.36 14.294-52.954 51.655 63.675 129.3 105.258 216.365 109.807-1.624-7.797-2.599-15.918-2.599-24.04 0-57.828 46.782-104.934 104.934-104.934 30.213 0 57.502 12.67 76.67 33.137 23.715-4.548 46.456-13.32 66.599-25.34-7.798 24.366-24.366 44.833-46.132 57.827 21.117-2.273 41.584-8.122 60.426-16.243-14.292 20.791-32.161 39.308-52.628 54.253z" />
              </svg>
            </a>
            <a href="#" className="icon">
              <svg xmlns="http://www.w3.org/2000/svg" height="1em" viewBox="0 0 448 512">
                <path d="M416 32H31.9C14.3 32 0 46.5 0 64.3v383.4C0 465.5 14.3 480 31.9 480H416c17.6 0 32-14.5 32-32.3V64.3c0-17.8-14.4-32.3-32-32.3zM135.4 416H69V202.2h66.5V416zm-33.2-243c-21.3 0-38.5-17.3-38.5-38.5S80.9 96 102.2 96c21.2 0 38.5 17.3 38.5 38.5 0 21.3-17.2 38.5-38.5 38.5zm282.1 243h-66.4V312c0-24.8-.5-56.7-34.5-56.7-34.6 0-39.9 27-39.9 54.9V416h-66.4V202.2h63.7v29.2h.9c8.9-16.8 30.6-34.5 62.9-34.5 67.2 0 79.7 44.3 79.7 101.9V416z" />
              </svg>
            </a>
          </div>
          <span>or use your email password</span>
          <input
            type="text"
            placeholder="Name"
            onChange={(e) => setMail(e.target.value)}
            value={mail} />
          <input
            type="password"
            placeholder="Password"
            onChange={(e) => setPassword(e.target.value)}
            value={password} />
          <a href="#">Olvidaste tu Contaseña?</a>
          <button
            type='submit'
            disabled={isLoading}>
            {isLoading && <span>Verificando...</span>}
            {!isLoading && <span>Ingresar</span>}
          </button>
        </form>
      </div>
      <div className="toggle-container">
        <div className="toggle">
          <div className="toggle-panel toggle-left">
            <h1>Bienvenido a BananaTV</h1>
            <p>Completa todos los campos para crear una nueva cuenta</p>
            <p>Ya tenes cuenta?</p>
            <button className="hidden" id="login">Inicia Sesion</button>
          </div>
          <div className="toggle-panel toggle-right">
            <h1>Bienvenido de Nuevo!</h1>
            <p>Ingresa a tu cuenta con tus datos</p>
            <p>No tenes cuenta?</p>
            <button className="hidden" id="register">Registrase</button>
          </div>
        </div>
      </div>
    </div>
  )
}

export default page