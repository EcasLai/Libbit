package com.example.libbit.model

import android.content.ContentValues.TAG
import android.provider.SyncStateContract
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libbit.contracts.SimpleLibrary_sol_SimpleLibrary
import com.example.libbit.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.tx.gas.StaticGasProvider


class LibraryViewModel : ViewModel(){

    private var _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> = _books
    private val _contractAddress = MutableLiveData<String>()
    val contractAddress: LiveData<String> = _contractAddress


    private val gasProvider =
        StaticGasProvider(DefaultGasProvider.GAS_PRICE, DefaultGasProvider.GAS_LIMIT)


    fun deploySmartContract(credentials: Credentials) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val web3j =
                        Web3j.build(HttpService(Constants.GOERLI_TESTNET))
                    val contractAddress =
                        SimpleLibrary_sol_SimpleLibrary.deploy(web3j, credentials, gasProvider).send().contractAddress
                    _contractAddress.postValue(contractAddress)
                } catch (e: Exception) {
                    _contractAddress.postValue("-1")
                    Log.d(TAG, "deploySmartContract: Exception: ${e.message}")
                }
            }
        }
    }

    fun loadSmartContractData(credentials: Credentials, deployedContractAddress: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val web3j = Web3j.build(HttpService(Constants.GOERLI_TESTNET))
                    val libraryContract = SimpleLibrary_sol_SimpleLibrary.load(
                        deployedContractAddress, web3j, credentials, DefaultGasProvider.GAS_PRICE, DefaultGasProvider.GAS_LIMIT
                    )
                    Log.d(TAG, "loadSmartContractData: Is Contract Valid: ${libraryContract.isValid}")

                } catch (e: Exception) {
                    Log.d(TAG, "loadSmartContractData: Exception: ${e.message}")
                }
            }
        }
    }

    fun mapBlockchainBookToAppBook(blockchainBook: BlockchainBook): Book {
        return Book(
            id = blockchainBook.bookId,
            title = blockchainBook.title,
            author = blockchainBook.author,
            // Set default or null values for the remaining fields
            isbn = null,
            bookImage = null,
            description = null,
            price = null,
            bookUrl = null,
            genre = null,
            type = null,
            status = null
        )
    }
}