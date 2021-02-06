package com.suftnet.v12.api

object Config {

    object Url {

        const val HOST = "http://kcmkcm-001-site9.btempurl.com/api/"
        //const val HOST = "http://192.168.0.11:8082/api/"
        object Account{
            const val Login ="${HOST}account/login"
        }
        object Produce{
            const val create ="${HOST}produce/create"
            const val edit ="${HOST}produce/edit"
            const val delete ="${HOST}produce/delete"
            const val fetch ="${HOST}produce/fetch"
        }
        object Seller{
            const val create ="${HOST}seller/create"
            const val fetch ="${HOST}seller/fetch"
        }
        object Buyer{
            const val create ="${HOST}buyer/create"
            const val fetch ="${HOST}buyer/fetch"
        }
        object Logistic{
            const val create ="${HOST}driver/create"
            const val fetch ="${HOST}driver/fetch"
        }
        object Market{
            const val fetch ="${HOST}market/fetch"
            const val getBy ="${HOST}market/getBy"
        }
        object Order{
            const val create ="${HOST}order/create"
            const val pendingOrders ="${HOST}order/pendingOrders"
            const val completedOrders ="${HOST}order/completedOrders"
        }
    }
}


