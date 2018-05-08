package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.jgit.revwalk.RevCommit;

import refdiff.core.rm2.model.refactoring.SDRefactoring;

public class CSV {
	
	private FileWriter writer;
	
	public CSV(File file) throws IOException {
		writer = new FileWriter(file);
		writer.write("Number;Commit;Parent;Msg_Commit;Refatoramento;Antes;Depois;Descricao_Completa");
		writer.flush();
	}
	
	public void addCSV(int number,RevCommit commit, SDRefactoring refactoring) throws IOException{
		writer.write("\n"+number);
		writer.write(";"+commit.getName());
		writer.write(";"+commit.getParent(0).getName());
		writer.write(";\""+commit.getShortMessage()+"\"");
		writer.write(";"+refactoring.getName());
		writer.write(";"+refactoring.getEntityBefore().fullName().replace(" ", "").replace("#", "."));
		writer.write(";"+refactoring.getEntityAfter().fullName().replace(" ", "").replace("#", "."));
		writer.write(";"+refactoring);
		writer.flush();
		
	}
	
	@Deprecated
	public void addCsv(int number,RevCommit commit, SDRefactoring refactoring) throws Exception{
		writer.write("\n"+number);
		writer.write(";"+commit.getName());
		writer.write(";"+commit.getParent(0).getName());
		writer.write(";\""+commit.getShortMessage()+"\"");
		String[] dados;
		String antes,depois,localizacaoOrigem,localizacaoDestino; 
		switch(refactoring.getName()){
			case "Extract Method":
				writer.write(";Metodo");
				writer.write(";Extract Method");
				dados=refactoring.toString().split(" extracted from ");
				depois=dados[0].substring(15);
				dados=dados[1].split(" in class ");
				localizacaoOrigem=dados[1];
				antes=dados[0];
				writer.write(";"+antes);
				writer.write(";"+localizacaoOrigem);
				writer.write(";"+depois+";");
				writer.write(";"+refactoring.toString());
				break;
			case "Inline Method":
				writer.write(";Metodo");
				writer.write(";Inline Method");
				dados=refactoring.toString().split(" inlined to ");
				antes=dados[0].substring(14);
				dados=dados[1].split(" in class ");
				localizacaoOrigem=dados[1];
				depois=dados[0];
				writer.write(";"+antes);
				writer.write(";"+localizacaoOrigem);
				writer.write(";"+depois+";");
				writer.write(";"+refactoring.toString());
				break;
			case "Rename Method":
				writer.write(";Metodo");
				writer.write(";Rename Method");
				dados=refactoring.toString().split(" renamed to ");
				antes=dados[0].substring(14);
				dados=dados[1].split(" in class ");
				localizacaoOrigem=dados[1];
				depois=dados[0];
				writer.write(";"+antes);
				writer.write(";"+localizacaoOrigem);
				writer.write(";"+depois+";");
				writer.write(";"+refactoring.toString());
				break;
			case "Move Method":
				writer.write(";Metodo");
				writer.write(";Move Method");
				dados=refactoring.toString().split(" from class ");
				antes=dados[0].substring(11);
				localizacaoOrigem=dados[1].split(" to ")[0];
				localizacaoDestino=dados[2];
				writer.write(";"+antes);
				writer.write(";"+localizacaoOrigem);
				writer.write(";;"+localizacaoDestino);
				writer.write(";"+refactoring.toString());
				break;
			case "Rename Class":
				writer.write(";Classe");
				writer.write(";Rename Class");
				dados=refactoring.toString().split(" renamed to ");
				antes=dados[0].substring(13);
				depois=dados[1];
				writer.write(";"+antes+";");
				writer.write(";"+depois+";");
				writer.write(";"+refactoring.toString());
				break;
			case "Move Class":
				writer.write(";Classe");
				writer.write(";Move Class");
				dados=refactoring.toString().split(" moved to ");
				localizacaoOrigem=dados[0].substring(11);
				localizacaoDestino=dados[1];
				writer.write(";;"+localizacaoOrigem);
				writer.write(";;"+localizacaoDestino);
				writer.write(";"+refactoring.toString());
				break;
			case "Move And Rename Class":
				writer.write(";Classe");
				writer.write(";Move And Rename Class");
				dados=refactoring.toString().split(" moved to ");
				localizacaoOrigem=dados[0].substring(22);
				localizacaoDestino=dados[1];
				writer.write(";;"+localizacaoOrigem);
				writer.write(";;"+localizacaoDestino);
				writer.write(";"+refactoring.toString());
				break;
			case "Extract Superclass":
				writer.write(";Classe");
				writer.write(";Extract Superclass");
				dados=refactoring.toString().split(" from class ");
				localizacaoDestino=dados[0].substring(19);
				localizacaoOrigem=dados[1];
				writer.write(";;"+localizacaoOrigem);
				writer.write(";;"+localizacaoDestino);
				writer.write(";"+refactoring.toString());
				break;
			case "Extract Interface":
				writer.write(";Classe");
				writer.write(";Extract Interface");
				dados=refactoring.toString().split(" from class ");
				localizacaoDestino=dados[0].substring(18);
				localizacaoOrigem=dados[1];
				writer.write(";;"+localizacaoOrigem);
				writer.write(";;"+localizacaoDestino);
				writer.write(";"+refactoring.toString());
				break;
			case "Move Attribute":
				writer.write(";Classe");
				writer.write(";Move Attribute");
				dados=refactoring.toString().split(" from class ");
				antes=dados[0].substring(15);
				dados=dados[1].split(" to class ");
				localizacaoOrigem=dados[0];
				localizacaoDestino=dados[1];
				writer.write(";"+antes);
				writer.write(";"+localizacaoOrigem);
				writer.write(";;"+localizacaoDestino);
				writer.write(";"+refactoring.toString());
				break;
			case "Pull Up Method":
				writer.write(";Classe");
				writer.write(";Pull Up Method");
				dados=refactoring.toString().split(" from class ");
				antes=dados[0].substring(15);
				localizacaoDestino=dados[2];
				dados=dados[1].split(" to ");
				depois=dados[1];
				localizacaoOrigem=dados[0];
				writer.write(";"+antes);
				writer.write(";"+localizacaoOrigem);
				writer.write(";"+depois);
				writer.write(";"+localizacaoDestino);
				writer.write(";"+refactoring.toString());
				break;
			case "Pull Up Attribute":
				writer.write(";Classe");
				writer.write(";Pull Up Attribute");
				dados=refactoring.toString().split(" from class ");
				antes=dados[0].substring(18);
				dados=dados[1].split(" to class ");
				localizacaoOrigem=dados[0];
				localizacaoDestino=dados[1];
				writer.write(";"+antes);
				writer.write(";"+localizacaoOrigem);
				writer.write(";;"+localizacaoDestino);
				writer.write(";"+refactoring.toString());
				break;
			case "Push Down Method":
				writer.write(";Classe");
				writer.write(";Push Down Method");
				dados=refactoring.toString().split(" from class ");
				antes=dados[0].substring(17);
				localizacaoDestino=dados[2];
				dados=dados[1].split(" to ");
				depois=dados[1];
				localizacaoOrigem=dados[0];
				writer.write(";"+antes);
				writer.write(";"+localizacaoOrigem);
				writer.write(";"+depois);
				writer.write(";"+localizacaoDestino);
				writer.write(";"+refactoring.toString());
				break;
			case "Push Down Attribute":
				writer.write(";Classe");
				writer.write(";Push Down Attribute");
				dados=refactoring.toString().split(" from class ");
				antes=dados[0].substring(20);
				dados=dados[1].split(" to class ");
				localizacaoOrigem=dados[0];
				localizacaoDestino=dados[1];
				writer.write(";"+antes);
				writer.write(";"+localizacaoOrigem);
				writer.write(";;"+localizacaoDestino);
				writer.write(";"+refactoring.toString());
				break;
			default:
				writer.write(";;"+refactoring.getName());
				writer.write(";;;;;"+refactoring.toString());
		}
		writer.flush();
		
	}
	
	public void close() throws IOException {
		writer.close();
	}
	
	

}
