package hello.web3j.service;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.math.BigInteger;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.Address;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Web3jSampleService {

	@Autowired
	private Web3j web3j;

	private String emptyAddress = "0x0000000000000000000000000000000000000000";
    String contractAddress = "0xe880141c45d66a131c99dacc88dbc32f85b454b2";

	public String getClientVersion() throws IOException {
		Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
		return web3ClientVersion.getWeb3ClientVersion();
	}

	public String getTokenName() {
		

		String methodName = "name";
		String name = null;
		String fromAddr = emptyAddress;
		List<Type> inputParameters = new ArrayList<>();
		List<TypeReference<?>> outputParameters = new ArrayList<>();
		TypeReference<Utf8String> typeReference = new TypeReference<Utf8String>() {
		};
		outputParameters.add(typeReference);
		Function function = new Function(methodName, inputParameters, outputParameters);
		String data = FunctionEncoder.encode(function);
		Transaction transaction = Transaction.createEthCallTransaction(fromAddr, contractAddress, data);
		EthCall ethCall;
		try {
			ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
			List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
			name = results.get(0).getValue().toString();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return name;
	}
	
	public BigInteger getAddressBalance(String account)  {
	  
           BigInteger tokenBalance = BigInteger.ZERO;
           String methodName = "balanceOf";
           List<Type> inputParameters = Arrays.<Type>asList(new Address(account));
           List<TypeReference<?>> outputParameters = Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {});
    		Function function = new Function(methodName, inputParameters, outputParameters);
    		String data = FunctionEncoder.encode(function);
    		Transaction transaction = Transaction.createEthCallTransaction(account, contractAddress, data);
    		EthCall ethCall;
    		try {
    			ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).sendAsync().get();
    			List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
    			tokenBalance= (BigInteger)results.get(0).getValue();
    		} catch (InterruptedException | ExecutionException e) {
    			e.printStackTrace();
    		}
    		return tokenBalance;
         
    }

}