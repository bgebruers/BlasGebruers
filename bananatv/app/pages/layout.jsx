import NavAdmin from '@/components/NavAdmin'
import '../../public/assets/css/Global.css'


export default function PagesLayout({ children }) {
  return (
      <section>
        <NavAdmin />
        {children}
      </section>
  )
}
