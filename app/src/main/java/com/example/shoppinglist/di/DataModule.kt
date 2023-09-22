package com.example.shoppinglist.di

import com.example.shoppinglist.data.converters.NetworkConverter
import com.example.shoppinglist.data.network.NetworkClient
import com.example.shoppinglist.data.network.impl.NetworkClientImpl
import com.example.shoppinglist.data.repositories.ShopRepositoryImpl
import com.example.shoppinglist.domain.repositories.ShopRepository
import org.koin.dsl.module

val dataModule = module {

    single<NetworkConverter> { NetworkConverter() }

    single<NetworkClient> { NetworkClientImpl() }

    single<ShopRepository> { ShopRepositoryImpl(networkClient = get(), networkConverter = get()) }


}