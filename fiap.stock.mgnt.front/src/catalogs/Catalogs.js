import React from 'react'
import CatalogsTable from './CatalogsTable'
import CatalogForm from './CatalogForm'

const catalogs = props => {
    return (
        <div>
            <CatalogForm />

            <CatalogsTable />
        </div>
    )
}

export default catalogs