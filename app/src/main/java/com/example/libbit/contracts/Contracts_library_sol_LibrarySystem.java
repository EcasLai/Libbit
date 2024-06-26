package com.example.libbit.contracts;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple3;
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
public class Contracts_library_sol_LibrarySystem extends Contract {
    public static final String BINARY = "6080604052348015600e575f80fd5b505f80546001600160a01b031916331790556112cc8061002d5f395ff3fe608060405234801561000f575f80fd5b50600436106100a6575f3560e01c806373ca152a1161006e57806373ca152a1461014557806385c4777e14610158578063a00578651461016b578063b9b1e2841461017e578063e1409a4014610193578063f851a440146101a6575f80fd5b80632b9dfe80146100aa57806330369884146100d657806345e6df3d146101065780636e4a44c51461011b5780636ec4f4bf1461012e575b5f80fd5b6100bd6100b8366004610dd8565b6101d0565b6040516100cd9493929190610e40565b60405180910390f35b6100e96100e4366004610e78565b61030f565b6040805193845260208401929092521515908201526060016100cd565b610119610114366004610ed2565b61036c565b005b610119610129366004610dd8565b61052c565b61013760025481565b6040519081526020016100cd565b610119610153366004610f5c565b61063c565b610119610166366004610dd8565b61084b565b610119610179366004610fa6565b610884565b610186610be6565b6040516100cd9190610fe8565b6101376101a1366004610dd8565b610c72565b5f546101b8906001600160a01b031681565b6040516001600160a01b0390911681526020016100cd565b80516020818301810180516001825292820191909301209152805481906101f690611001565b80601f016020809104026020016040519081016040528092919081815260200182805461022290611001565b801561026d5780601f106102445761010080835404028352916020019161026d565b820191905f5260205f20905b81548152906001019060200180831161025057829003601f168201915b50505050509080600101805461028290611001565b80601f01602080910402602001604051908101604052809291908181526020018280546102ae90611001565b80156102f95780601f106102d0576101008083540402835291602001916102f9565b820191905f5260205f20905b8154815290600101906020018083116102dc57829003601f168201915b5050505050908060020154908060030154905084565b5f805f806001866040516103239190611033565b90815260408051602092819003830190206001600160a01b03979097165f908152600490970190915290942080546001820154600290920154909791965060ff16945092505050565b5f546001600160a01b0316331461039e5760405162461bcd60e51b815260040161039590611049565b60405180910390fd5b5f8451116103ee5760405162461bcd60e51b815260206004820152601760248201527f426f6f6b2049442063616e6e6f7420626520656d7074790000000000000000006044820152606401610395565b6001846040516103fe9190611033565b908152604051908190036020019020805461041890611001565b1590506104755760405162461bcd60e51b815260206004820152602560248201527f426f6f6b20776974682073706563696669656420494420616c72656164792065604482015264786973747360d81b6064820152608401610395565b5f6001856040516104869190611033565b9081526040519081900360200190209050806104a285826110d6565b50600181016104b184826110d6565b5060028082018390555f60038301819055815491906104cf836111aa565b9190505550846040516104e29190611033565b60405180910390207f32bbea680b567e6c2a9bdf68561848fac73b5f056a1198d3544dd18e87a8a19d85858560405161051d939291906111c2565b60405180910390a25050505050565b5f546001600160a01b031633146105555760405162461bcd60e51b815260040161039590611049565b806001816040516105669190611033565b908152604051908190036020019020805461058090611001565b90505f036105a05760405162461bcd60e51b8152600401610395906111f7565b6001826040516105b09190611033565b9081526040519081900360200190205f6105ca8282610ce9565b6105d7600183015f610ce9565b505f60028281018290556003909201819055815491906105f68361121f565b9190505550816040516106099190611033565b604051908190038120907f2fbe5051f01f13ba3c0f1836a7495627a4aa5dc1bed39f88f01b531dc0c7756f905f90a25050565b8260018160405161064d9190611033565b908152604051908190036020019020805461066790611001565b90505f036106875760405162461bcd60e51b8152600401610395906111f7565b82845f821180156106b957506001816040516106a39190611033565b9081526020016040518091039020600201548211155b6107105760405162461bcd60e51b815260206004820152602260248201527f496e76616c6964206e756d626572206f6620636f7069657320746f20626f72726044820152616f7760f01b6064820152608401610395565b5f6001876040516107219190611033565b9081526040519081900360200190209050335f5b878110156107b957604051806060016040528042815260200142896201518061075e9190611234565b6107689190611251565b81525f60209182018190526001600160a01b038516815260048601825260409081902083518155918301516001808401919091559201516002909101805460ff191691151591909117905501610735565b5086826002015f8282546107cd9190611264565b9250508190555086826003015f8282546107e79190611251565b90915550506040516107fa908990611033565b60408051918290038220898352426020840152908201889052907fa5c6a4fb29cad99c7f19720c43061d3a5f1ba1aab2dadf9634be26a929a8437d9060600160405180910390a25050505050505050565b5f546001600160a01b031633146108745760405162461bcd60e51b815260040161039590611049565b600361088082826110d6565b5050565b816001816040516108959190611033565b90815260405190819003602001902080546108af90611001565b90505f036108cf5760405162461bcd60e51b8152600401610395906111f7565b81835f8211801561090157506001816040516108eb9190611033565b9081526020016040518091039020600301548211155b6109585760405162461bcd60e51b815260206004820152602260248201527f496e76616c6964206e756d626572206f6620636f7069657320746f2072657475604482015261393760f11b6064820152608401610395565b84845f60018360405161096b9190611033565b9081526040805160209281900383019020335f9081526004909101909252902054116109d95760405162461bcd60e51b815260206004820152601f60248201527f596f752068617665206e6f7420626f72726f776564207468697320626f6f6b006044820152606401610395565b6001826040516109e99190611033565b908152602001604051809103902060030154811115610a635760405162461bcd60e51b815260206004820152603060248201527f596f752068617665206e6f7420626f72726f77656420656e6f75676820636f7060448201526f696573206f66207468697320626f6f6b60801b6064820152608401610395565b5f600188604051610a749190611033565b9081526040519081900360200190209050335f805b89811015610b1b576001600160a01b0383165f9081526004850160205260409020600281015460ff16158015610ac25750806001015442115b15610b125762015180816001015442610adb9190611264565b610ae59190611277565b610af69066038d7ea4c68000611234565b610b009084611251565b60028201805460ff1916600117905592505b50600101610a89565b5088836002015f828254610b2f9190611251565b9250508190555088836003015f828254610b499190611264565b90915550508015610b8c575f80546040516001600160a01b039091169183156108fc02918491818181858888f19350505050158015610b8a573d5f803e3d5ffd5b505b89604051610b9a9190611033565b604080519182900382208b835260208301849052917fd0dc696965d395963f3afcbb13455b9f466667fd6c2dacc2f13d4d8cb8c2ab44910160405180910390a250505050505050505050565b60038054610bf390611001565b80601f0160208091040260200160405190810160405280929190818152602001828054610c1f90611001565b8015610c6a5780601f10610c4157610100808354040283529160200191610c6a565b820191905f5260205f20905b815481529060010190602001808311610c4d57829003601f168201915b505050505081565b5f81600181604051610c849190611033565b9081526040519081900360200190208054610c9e90611001565b90505f03610cbe5760405162461bcd60e51b8152600401610395906111f7565b600183604051610cce9190611033565b90815260200160405180910390206002015491505b50919050565b508054610cf590611001565b5f825580601f10610d04575050565b601f0160209004905f5260205f2090810190610d209190610d23565b50565b5b80821115610d37575f8155600101610d24565b5090565b634e487b7160e01b5f52604160045260245ffd5b5f82601f830112610d5e575f80fd5b813567ffffffffffffffff80821115610d7957610d79610d3b565b604051601f8301601f19908116603f01168101908282118183101715610da157610da1610d3b565b81604052838152866020858801011115610db9575f80fd5b836020870160208301375f602085830101528094505050505092915050565b5f60208284031215610de8575f80fd5b813567ffffffffffffffff811115610dfe575f80fd5b610e0a84828501610d4f565b949350505050565b5f81518084528060208401602086015e5f602082860101526020601f19601f83011685010191505092915050565b608081525f610e526080830187610e12565b8281036020840152610e648187610e12565b604084019590955250506060015292915050565b5f8060408385031215610e89575f80fd5b823567ffffffffffffffff811115610e9f575f80fd5b610eab85828601610d4f565b92505060208301356001600160a01b0381168114610ec7575f80fd5b809150509250929050565b5f805f8060808587031215610ee5575f80fd5b843567ffffffffffffffff80821115610efc575f80fd5b610f0888838901610d4f565b95506020870135915080821115610f1d575f80fd5b610f2988838901610d4f565b94506040870135915080821115610f3e575f80fd5b50610f4b87828801610d4f565b949793965093946060013593505050565b5f805f60608486031215610f6e575f80fd5b833567ffffffffffffffff811115610f84575f80fd5b610f9086828701610d4f565b9660208601359650604090950135949350505050565b5f8060408385031215610fb7575f80fd5b823567ffffffffffffffff811115610fcd575f80fd5b610fd985828601610d4f565b95602094909401359450505050565b602081525f610ffa6020830184610e12565b9392505050565b600181811c9082168061101557607f821691505b602082108103610ce357634e487b7160e01b5f52602260045260245ffd5b5f82518060208501845e5f920191825250919050565b60208082526021908201527f4f6e6c792061646d696e2063616e2063616c6c20746869732066756e6374696f6040820152603760f91b606082015260800190565b601f8211156110d157805f5260205f20601f840160051c810160208510156110af5750805b601f840160051c820191505b818110156110ce575f81556001016110bb565b50505b505050565b815167ffffffffffffffff8111156110f0576110f0610d3b565b611104816110fe8454611001565b8461108a565b602080601f831160018114611137575f84156111205750858301515b5f19600386901b1c1916600185901b17855561118e565b5f85815260208120601f198616915b8281101561116557888601518255948401946001909101908401611146565b508582101561118257878501515f19600388901b60f8161c191681555b505060018460011b0185555b505050505050565b634e487b7160e01b5f52601160045260245ffd5b5f600182016111bb576111bb611196565b5060010190565b606081525f6111d46060830186610e12565b82810360208401526111e68186610e12565b915050826040830152949350505050565b6020808252600e908201526d109bdbdac81b9bdd08199bdd5b9960921b604082015260600190565b5f8161122d5761122d611196565b505f190190565b808202811582820484141761124b5761124b611196565b92915050565b8082018082111561124b5761124b611196565b8181038181111561124b5761124b611196565b5f8261129157634e487b7160e01b5f52601260045260245ffd5b50049056fea2646970667358221220a1652bd61f0d992225709c1316d74e45e439c31cabcca9ce71fe797a109a76c964736f6c63430008190033";

    private static String librariesLinkedBinary;

    public static final String FUNC_ADDBOOK = "addBook";

    public static final String FUNC_ADMIN = "admin";

    public static final String FUNC_BOOKS = "books";

    public static final String FUNC_BORROWBOOK = "borrowBook";

    public static final String FUNC_CHECKAVAILABILITY = "checkAvailability";

    public static final String FUNC_GETBORROWINFO = "getBorrowInfo";

    public static final String FUNC_PDFHASH = "pdfHash";

    public static final String FUNC_REMOVEBOOK = "removeBook";

    public static final String FUNC_RETURNBOOK = "returnBook";

    public static final String FUNC_STOREPDFHASH = "storePDFHash";

    public static final String FUNC_TOTALBOOKS = "totalBooks";

    public static final Event BOOKADDED_EVENT = new Event("BookAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event BOOKBORROWED_EVENT = new Event("BookBorrowed", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event BOOKREMOVED_EVENT = new Event("BookRemoved", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>(true) {}));
    ;

    public static final Event BOOKRETURNED_EVENT = new Event("BookReturned", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected Contracts_library_sol_LibrarySystem(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Contracts_library_sol_LibrarySystem(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Contracts_library_sol_LibrarySystem(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Contracts_library_sol_LibrarySystem(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<BookAddedEventResponse> getBookAddedEvents(TransactionReceipt transactionReceipt) {
        List<BookAddedEventResponse> responses = new ArrayList<>();

        // Get the logs from the transaction receipt
        List<Log> logs = transactionReceipt.getLogs();

        // Filter logs for the BOOKADDED_EVENT
        for (Log log : logs) {
            if (log.getTopics().get(0).equals(BOOKADDED_EVENT)) {
                Contract.EventValuesWithLog eventValuesWithLog = staticExtractEventParametersWithLog(BOOKADDED_EVENT, log);
                if (eventValuesWithLog != null) {
                    BookAddedEventResponse typedResponse = new BookAddedEventResponse();
                    typedResponse.log = eventValuesWithLog.getLog();
                    typedResponse.bookId = (byte[]) eventValuesWithLog.getIndexedValues().get(0).getValue();
                    typedResponse.title = (String) eventValuesWithLog.getNonIndexedValues().get(0).getValue();
                    typedResponse.author = (String) eventValuesWithLog.getNonIndexedValues().get(1).getValue();
                    typedResponse.copiesAvailable = (BigInteger) eventValuesWithLog.getNonIndexedValues().get(2).getValue();
                    responses.add(typedResponse);
                }
            }
        }
        return responses;
    }

    public static BookAddedEventResponse getBookAddedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(BOOKADDED_EVENT, log);
        BookAddedEventResponse typedResponse = new BookAddedEventResponse();
        typedResponse.log = log;
        typedResponse.bookId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.title = (String) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.author = (String) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.copiesAvailable = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<BookAddedEventResponse> bookAddedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getBookAddedEventFromLog(log));
    }

    public Flowable<BookAddedEventResponse> bookAddedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(BOOKADDED_EVENT));
        return bookAddedEventFlowable(filter);
    }

    public static List<BookBorrowedEventResponse> getBookBorrowedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = new ArrayList<>();

        // Get the logs from the transaction receipt
        List<Log> logs = transactionReceipt.getLogs();

        // Filter logs for the BOOKBORROWED_EVENT
        for (Log log : logs) {
            if (log.getTopics().get(0).equals(BOOKBORROWED_EVENT)) {
                Contract.EventValuesWithLog eventValuesWithLog = staticExtractEventParametersWithLog(BOOKBORROWED_EVENT, log);
                if (eventValuesWithLog != null) {
                    valueList.add(eventValuesWithLog);
                }
            }
        }

        ArrayList<BookBorrowedEventResponse> responses = new ArrayList<>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            BookBorrowedEventResponse typedResponse = new BookBorrowedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.bookId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.copiesBorrowed = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.borrowDate = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.dueDate = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static BookBorrowedEventResponse getBookBorrowedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(BOOKBORROWED_EVENT, log);
        BookBorrowedEventResponse typedResponse = new BookBorrowedEventResponse();
        typedResponse.log = log;
        typedResponse.bookId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.copiesBorrowed = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.borrowDate = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        typedResponse.dueDate = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
        return typedResponse;
    }

    public Flowable<BookBorrowedEventResponse> bookBorrowedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getBookBorrowedEventFromLog(log));
    }

    public Flowable<BookBorrowedEventResponse> bookBorrowedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(BOOKBORROWED_EVENT));
        return bookBorrowedEventFlowable(filter);
    }

    public static List<BookRemovedEventResponse> getBookRemovedEvents(TransactionReceipt transactionReceipt) {
        List<BookRemovedEventResponse> responses = new ArrayList<>();

        // Get the logs from the transaction receipt
        List<Log> logs = transactionReceipt.getLogs();

        // Filter logs for the BOOKREMOVED_EVENT
        for (Log log : logs) {
            if (log.getTopics().get(0).equals(BOOKREMOVED_EVENT)) {
                Contract.EventValuesWithLog eventValuesWithLog = staticExtractEventParametersWithLog(BOOKREMOVED_EVENT, log);
                if (eventValuesWithLog != null) {
                    BookRemovedEventResponse typedResponse = new BookRemovedEventResponse();
                    typedResponse.log = eventValuesWithLog.getLog();
                    typedResponse.bookId = (byte[]) eventValuesWithLog.getIndexedValues().get(0).getValue();
                    responses.add(typedResponse);
                }
            }
        }
        return responses;
    }

    public static BookRemovedEventResponse getBookRemovedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(BOOKREMOVED_EVENT, log);
        BookRemovedEventResponse typedResponse = new BookRemovedEventResponse();
        typedResponse.log = log;
        typedResponse.bookId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<BookRemovedEventResponse> bookRemovedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getBookRemovedEventFromLog(log));
    }

    public Flowable<BookRemovedEventResponse> bookRemovedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(BOOKREMOVED_EVENT));
        return bookRemovedEventFlowable(filter);
    }

    public static List<BookReturnedEventResponse> getBookReturnedEvents(TransactionReceipt transactionReceipt) {
        List<BookReturnedEventResponse> responses = new ArrayList<>();

        // Get the logs from the transaction receipt
        List<Log> logs = transactionReceipt.getLogs();

        // Filter logs for the BOOKRETURNED_EVENT
        for (Log log : logs) {
            if (log.getTopics().get(0).equals(BOOKRETURNED_EVENT)) {
                Contract.EventValuesWithLog eventValuesWithLog = staticExtractEventParametersWithLog(BOOKRETURNED_EVENT, log);
                if (eventValuesWithLog != null) {
                    BookReturnedEventResponse typedResponse = new BookReturnedEventResponse();
                    typedResponse.log = eventValuesWithLog.getLog();
                    typedResponse.bookId = (byte[]) eventValuesWithLog.getIndexedValues().get(0).getValue();
                    typedResponse.copiesReturned = (BigInteger) eventValuesWithLog.getNonIndexedValues().get(0).getValue();
                    typedResponse.lateFee = (BigInteger) eventValuesWithLog.getNonIndexedValues().get(1).getValue();
                    responses.add(typedResponse);
                }
            }
        }
        return responses;
    }

    public static BookReturnedEventResponse getBookReturnedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(BOOKRETURNED_EVENT, log);
        BookReturnedEventResponse typedResponse = new BookReturnedEventResponse();
        typedResponse.log = log;
        typedResponse.bookId = (byte[]) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.copiesReturned = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.lateFee = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<BookReturnedEventResponse> bookReturnedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getBookReturnedEventFromLog(log));
    }

    public Flowable<BookReturnedEventResponse> bookReturnedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(BOOKRETURNED_EVENT));
        return bookReturnedEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> addBook(String _bookId, String _title, String _author, BigInteger _copiesAvailable) {
        final Function function = new Function(
                FUNC_ADDBOOK, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_bookId), 
                new org.web3j.abi.datatypes.Utf8String(_title), 
                new org.web3j.abi.datatypes.Utf8String(_author), 
                new org.web3j.abi.datatypes.generated.Uint256(_copiesAvailable)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> admin() {
        final Function function = new Function(FUNC_ADMIN,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<Tuple4<String, String, BigInteger, BigInteger>> books(String param0) {
        final Function function = new Function(FUNC_BOOKS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple4<String, String, BigInteger, BigInteger>>(function,
                new Callable<Tuple4<String, String, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple4<String, String, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<String, String, BigInteger, BigInteger>(
                                (String) results.get(0).getValue(),
                                (String) results.get(1).getValue(),
                                (BigInteger) results.get(2).getValue(),
                                (BigInteger) results.get(3).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> borrowBook(String _bookId, BigInteger _copiesToBorrow, BigInteger _dueDate) {
        final Function function = new Function(
                FUNC_BORROWBOOK, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_bookId), 
                new org.web3j.abi.datatypes.generated.Uint256(_copiesToBorrow), 
                new org.web3j.abi.datatypes.generated.Uint256(_dueDate)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> checkAvailability(String _bookId) {
        final Function function = new Function(FUNC_CHECKAVAILABILITY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_bookId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple3<BigInteger, BigInteger, Boolean>> getBorrowInfo(String _bookId, String _borrower) {
        final Function function = new Function(FUNC_GETBORROWINFO, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_bookId), 
                new org.web3j.abi.datatypes.Address(160, _borrower)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
        return new RemoteFunctionCall<Tuple3<BigInteger, BigInteger, Boolean>>(function,
                new Callable<Tuple3<BigInteger, BigInteger, Boolean>>() {
                    @Override
                    public Tuple3<BigInteger, BigInteger, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<BigInteger, BigInteger, Boolean>(
                                (BigInteger) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (Boolean) results.get(2).getValue());
                    }
                });
    }

    public RemoteFunctionCall<String> pdfHash() {
        final Function function = new Function(FUNC_PDFHASH, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> removeBook(String _bookId) {
        final Function function = new Function(
                FUNC_REMOVEBOOK, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_bookId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> returnBook(String _bookId, BigInteger _copiesToReturn) {
        final Function function = new Function(
                FUNC_RETURNBOOK, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_bookId), 
                new org.web3j.abi.datatypes.generated.Uint256(_copiesToReturn)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> storePDFHash(String _ipfsHash) {
        final Function function = new Function(
                FUNC_STOREPDFHASH, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_ipfsHash)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> totalBooks() {
        final Function function = new Function(FUNC_TOTALBOOKS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    @Deprecated
    public static Contracts_library_sol_LibrarySystem load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Contracts_library_sol_LibrarySystem(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Contracts_library_sol_LibrarySystem load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Contracts_library_sol_LibrarySystem(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Contracts_library_sol_LibrarySystem load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Contracts_library_sol_LibrarySystem(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Contracts_library_sol_LibrarySystem load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Contracts_library_sol_LibrarySystem(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Contracts_library_sol_LibrarySystem> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Contracts_library_sol_LibrarySystem.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    public static RemoteCall<Contracts_library_sol_LibrarySystem> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Contracts_library_sol_LibrarySystem.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<Contracts_library_sol_LibrarySystem> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Contracts_library_sol_LibrarySystem.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<Contracts_library_sol_LibrarySystem> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Contracts_library_sol_LibrarySystem.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
    }
//
//    public static void linkLibraries(List<Contract.LinkReference> references) {
//        librariesLinkedBinary = linkBinaryWithReferences(BINARY, references);
//    }

    public static String getDeploymentBinary() {
        if (librariesLinkedBinary != null) {
            return librariesLinkedBinary;
        } else {
            return BINARY;
        }
    }

    public static class BookAddedEventResponse extends BaseEventResponse {
        public byte[] bookId;

        public String title;

        public String author;

        public BigInteger copiesAvailable;
    }

    public static class BookBorrowedEventResponse extends BaseEventResponse {
        public byte[] bookId;

        public BigInteger copiesBorrowed;

        public BigInteger borrowDate;

        public BigInteger dueDate;
    }

    public static class BookRemovedEventResponse extends BaseEventResponse {
        public byte[] bookId;
    }

    public static class BookReturnedEventResponse extends BaseEventResponse {
        public byte[] bookId;

        public BigInteger copiesReturned;

        public BigInteger lateFee;
    }
}
