package com.example.libbit.model

import android.content.ContentValues.TAG
import android.provider.SyncStateContract
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libbit.contracts.Contracts_library_sol_LibrarySystem
import com.example.libbit.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.tx.gas.StaticGasProvider
import java.math.BigInteger

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
                        Contracts_library_sol_LibrarySystem.deploy(web3j, credentials, gasProvider).send().contractAddress
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
                    val libraryContract = Contracts_library_sol_LibrarySystem.load(
                        deployedContractAddress, web3j, credentials, DefaultGasProvider.GAS_PRICE, DefaultGasProvider.GAS_LIMIT
                    )
                    Log.d(TAG, "loadSmartContractData: Is Contract Valid: ${libraryContract.isValid}")

                    // Retrieve total number of books
                    val totalBooks = libraryContract.totalBooks().send().toInt()

                    // Retrieve information about each book
                    val books = ArrayList<Book>()
                    for (i in 0 until totalBooks) {
                        val bookId = "bookId_$i"  // Generate or retrieve bookId appropriately
                        val bookInfo = libraryContract.books(bookId).send()

                        // Extract book information from the returned data
                        val title = bookInfo.component1()
                        val author = bookInfo.component2()
                        val copiesAvailable = bookInfo.component3().toInt()
                        val copiesBorrowed = bookInfo.component4().toInt()

                        // Create a BlockchainBook object
                        val blockchainBook = BlockchainBook(bookId, title, author, copiesAvailable, copiesBorrowed)

                        // Convert BlockchainBook to app's Book object
                        val appBook = mapBlockchainBookToAppBook(blockchainBook)

                        books.add(appBook)
                    }
                    // Update LiveData with the retrieved list of books
                    _books.postValue(books)
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