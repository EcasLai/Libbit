package com.example.libbit.contracts;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.5.3.
 */
@SuppressWarnings("rawtypes")
public class SimpleLibrary_sol_SimpleLibrary extends Contract {
    public static final String BINARY = "6080604052348015600e575f80fd5b506109f58061001c5f395ff3fe608060405234801561000f575f80fd5b5060043610610060575f3560e01c80636874404614610064578063a1dc589414610090578063ca5140c9146100a5578063d73e0c89146100b8578063d8a7233e146100c8578063e0ff5b8b146100db575b5f80fd5b6100776100723660046106b1565b6100ee565b60405161008794939291906106f6565b60405180910390f35b6100a361009e3660046106b1565b610242565b005b6100a36100b33660046106b1565b610359565b5f54604051908152602001610087565b6100a36100d63660046107d9565b61045e565b6100776100e93660046106b1565b61051e565b5f81815481106100fc575f80fd5b905f5260205f2090600302015f91509050805f01805461011b90610839565b80601f016020809104026020016040519081016040528092919081815260200182805461014790610839565b80156101925780601f1061016957610100808354040283529160200191610192565b820191905f5260205f20905b81548152906001019060200180831161017557829003601f168201915b5050505050908060010180546101a790610839565b80601f01602080910402602001604051908101604052809291908181526020018280546101d390610839565b801561021e5780601f106101f55761010080835404028352916020019161021e565b820191905f5260205f20905b81548152906001019060200180831161020157829003601f168201915b5050506002909301549192505060ff8116906001600160a01b036101009091041684565b5f54811061026b5760405162461bcd60e51b815260040161026290610871565b60405180910390fd5b5f818154811061027d5761027d61089f565b5f91825260209091206002600390920201015460ff166102d85760405162461bcd60e51b81526020600482015260166024820152752137b7b59034b9903737ba1030bb30b4b630b136329760511b6044820152606401610262565b5f8082815481106102eb576102eb61089f565b905f5260205f2090600302016002015f6101000a81548160ff021916908315150217905550335f82815481106103235761032361089f565b905f5260205f20906003020160020160016101000a8154816001600160a01b0302191690836001600160a01b0316021790555050565b5f5481106103795760405162461bcd60e51b815260040161026290610871565b336001600160a01b03165f82815481106103955761039561089f565b5f91825260209091206003909102016002015461010090046001600160a01b0316146104125760405162461bcd60e51b815260206004820152602660248201527f596f7520646f206e6f742068617665207468697320626f6f6b20636865636b65604482015265321037baba1760d11b6064820152608401610262565b60015f82815481106104265761042661089f565b905f5260205f2090600302016002015f6101000a81548160ff0219169083151502179055505f8082815481106103235761032361089f565b604080516080810182528381526020810183905260019181018290525f60608201819052805492830181558052805190916003027f290decd9548b62a8d60345a988386fc84ba6bc95484008f6362f93160ef3e563019081906104c190826108ff565b50602082015160018201906104d690826108ff565b506040820151600290910180546060909301516001600160a01b031661010002610100600160a81b0319921515929092166001600160a81b0319909316929092171790555050565b6060805f805f8054905085106105465760405162461bcd60e51b815260040161026290610871565b5f8086815481106105595761055961089f565b5f9182526020909120600390910201600281015481549192508291600183019160ff8116916101009091046001600160a01b031690849061059990610839565b80601f01602080910402602001604051908101604052809291908181526020018280546105c590610839565b80156106105780601f106105e757610100808354040283529160200191610610565b820191905f5260205f20905b8154815290600101906020018083116105f357829003601f168201915b5050505050935082805461062390610839565b80601f016020809104026020016040519081016040528092919081815260200182805461064f90610839565b801561069a5780601f106106715761010080835404028352916020019161069a565b820191905f5260205f20905b81548152906001019060200180831161067d57829003601f168201915b505050505092509450945094509450509193509193565b5f602082840312156106c1575f80fd5b5035919050565b5f81518084528060208401602086015e5f602082860101526020601f19601f83011685010191505092915050565b608081525f61070860808301876106c8565b828103602084015261071a81876106c8565b941515604084015250506001600160a01b039190911660609091015292915050565b634e487b7160e01b5f52604160045260245ffd5b5f82601f83011261075f575f80fd5b813567ffffffffffffffff8082111561077a5761077a61073c565b604051601f8301601f19908116603f011681019082821181831017156107a2576107a261073c565b816040528381528660208588010111156107ba575f80fd5b836020870160208301375f602085830101528094505050505092915050565b5f80604083850312156107ea575f80fd5b823567ffffffffffffffff80821115610801575f80fd5b61080d86838701610750565b93506020850135915080821115610822575f80fd5b5061082f85828601610750565b9150509250929050565b600181811c9082168061084d57607f821691505b60208210810361086b57634e487b7160e01b5f52602260045260245ffd5b50919050565b6020808252601490820152732137b7b5903237b2b9903737ba1032bc34b9ba1760611b604082015260600190565b634e487b7160e01b5f52603260045260245ffd5b601f8211156108fa57805f5260205f20601f840160051c810160208510156108d85750805b601f840160051c820191505b818110156108f7575f81556001016108e4565b50505b505050565b815167ffffffffffffffff8111156109195761091961073c565b61092d816109278454610839565b846108b3565b602080601f831160018114610960575f84156109495750858301515b5f19600386901b1c1916600185901b1785556109b7565b5f85815260208120601f198616915b8281101561098e5788860151825594840194600190910190840161096f565b50858210156109ab57878501515f19600388901b60f8161c191681555b505060018460011b0185555b50505050505056fea264697066735822122012b8e5b7245036d469b8f99a7497f17c6fec265bc21842633dc01b57bdb9072f64736f6c63430008190033";

    private static String librariesLinkedBinary;

    public static final String FUNC_ADDBOOK = "addBook";

    public static final String FUNC_BOOKS = "books";

    public static final String FUNC_CHECKOUTBOOK = "checkOutBook";

    public static final String FUNC_GETBOOK = "getBook";

    public static final String FUNC_GETBOOKCOUNT = "getBookCount";

    public static final String FUNC_RETURNBOOK = "returnBook";

    @Deprecated
    protected SimpleLibrary_sol_SimpleLibrary(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected SimpleLibrary_sol_SimpleLibrary(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected SimpleLibrary_sol_SimpleLibrary(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected SimpleLibrary_sol_SimpleLibrary(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<TransactionReceipt> addBook(String _title, String _author) {
        final Function function = new Function(
                FUNC_ADDBOOK, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_title), 
                new org.web3j.abi.datatypes.Utf8String(_author)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple4<String, String, Boolean, String>> books(BigInteger param0) {
        final Function function = new Function(FUNC_BOOKS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Bool>() {}, new TypeReference<Address>() {}));
        return new RemoteFunctionCall<Tuple4<String, String, Boolean, String>>(function,
                new Callable<Tuple4<String, String, Boolean, String>>() {
                    @Override
                    public Tuple4<String, String, Boolean, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<String, String, Boolean, String>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (Boolean) results.get(2).getValue(), 
                                (String) results.get(3).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> checkOutBook(BigInteger _bookId) {
        final Function function = new Function(
                FUNC_CHECKOUTBOOK, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_bookId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple4<String, String, Boolean, String>> getBook(BigInteger _bookId) {
        final Function function = new Function(FUNC_GETBOOK, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_bookId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Bool>() {}, new TypeReference<Address>() {}));
        return new RemoteFunctionCall<Tuple4<String, String, Boolean, String>>(function,
                new Callable<Tuple4<String, String, Boolean, String>>() {
                    @Override
                    public Tuple4<String, String, Boolean, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<String, String, Boolean, String>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (Boolean) results.get(2).getValue(), 
                                (String) results.get(3).getValue());
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> getBookCount() {
        final Function function = new Function(FUNC_GETBOOKCOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> returnBook(BigInteger _bookId) {
        final Function function = new Function(
                FUNC_RETURNBOOK, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_bookId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static SimpleLibrary_sol_SimpleLibrary load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new SimpleLibrary_sol_SimpleLibrary(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static SimpleLibrary_sol_SimpleLibrary load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SimpleLibrary_sol_SimpleLibrary(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static SimpleLibrary_sol_SimpleLibrary load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new SimpleLibrary_sol_SimpleLibrary(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static SimpleLibrary_sol_SimpleLibrary load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new SimpleLibrary_sol_SimpleLibrary(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<SimpleLibrary_sol_SimpleLibrary> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SimpleLibrary_sol_SimpleLibrary.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<SimpleLibrary_sol_SimpleLibrary> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SimpleLibrary_sol_SimpleLibrary.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<SimpleLibrary_sol_SimpleLibrary> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SimpleLibrary_sol_SimpleLibrary.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<SimpleLibrary_sol_SimpleLibrary> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SimpleLibrary_sol_SimpleLibrary.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

}
