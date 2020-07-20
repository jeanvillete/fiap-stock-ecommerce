import React from 'react'
import CatalogsTable from './CatalogsTable'
import CatalogForm from './CatalogForm'
import Alert from '../alert/Alert'

const catalogs = props => {
    return (
        <div>
            <CatalogForm />

            <Alert />

            <CatalogsTable />
        </div>
    )
}

export default catalogs