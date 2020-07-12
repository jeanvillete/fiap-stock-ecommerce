import React from 'react'
import AddressTable from './AddressesTable'
import AddressForm from './AddressForm'

const addresses = props => {
    return (
        <div>
            <AddressForm />
            
            <AddressTable />
        </div>
    )
}

export default addresses