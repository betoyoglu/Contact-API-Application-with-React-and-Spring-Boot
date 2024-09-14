import React from 'react'

const Header = ({toggleModdal, nbOfContacts}) => { //propsun daha kısa yolu
  return (
    <header className='header'>
        <div className='container'>
            <h3> Contact List ({nbOfContacts})</h3>
            <button onClick={()=> toggleModdal(true)} className='btn'>
                <i className='bi bi-plus-quare'></i> add new contact 
            </button>
        </div>
    </header>
  )
}

//toggleModal boolean bir argument alıcak ve Modalın açılması veya kapanması gerektiğinde bu fonksiyon çağrılıcak

export default Header