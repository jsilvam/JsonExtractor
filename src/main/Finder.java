package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import refdiff.core.RefDiff;
import refdiff.core.api.GitService;
import refdiff.core.rm2.model.refactoring.SDRefactoring;
import refdiff.core.util.GitServiceImpl;

public class Finder {
	
	private static int count = 0;
	
	private File folder;
	private File csvFolder;
	private Map<String,CSV> CSVs;
	private Map<String,Repository> repositories;
	private JsonHandler jHandler;
	
	
	public Finder(File jsonFile) throws IOException {
		folder = new File(System.getProperty("java.io.tmpdir") + "Projeto");
		String aux = System.getProperty("user.home") + File.separator + "Dropbox" + File.separator + "UFCG" + 
				File.separator + "Projeto" + File.separator + "Dados" + File.separator + "DataSet" + File.separator + "Part 1";
		csvFolder = new File(aux);
		CSVs = new HashMap<String,CSV>();
		repositories = new HashMap<String,Repository>();
		jHandler = new JsonHandler();
		jHandler.load(jsonFile);
	}
	
	public Finder(String repositoriesPath, String CSVsPath, File jsonFile) throws IOException {
		folder = new File(repositoriesPath);
		csvFolder = new File(CSVsPath);
		jHandler = new JsonHandler();
		jHandler.load(jsonFile);
	}
	
	public void analise(String url) throws Exception {
		String repositoryURL = getRepositoryURL(url);
		Repository repository = getRepository(repositoryURL);
		RevCommit commit = getCommit(url, repository);
		List<SDRefactoring> refactorings = extractRefactorings(repository, commit.getName());
		CSV csv = getCSV(repositoryURL);
		
		for (SDRefactoring refactoring : refactorings) {
			if(jHandler.isTruePositive(url, refactoring))
				csv.addCSV(count,commit,refactoring);
		}
		count++;
		repository.close();
	}
	
	private List<SDRefactoring> extractRefactorings(Repository repository, String commit){
		RefDiff refDiff = new RefDiff();
		List<SDRefactoring> refactorings = refDiff.detectAtCommit(repository, commit);
		return refactorings;
	}
	
	private RevCommit getCommit(String url, Repository repository) throws Exception {
		int index = url.lastIndexOf("/");
		String commitHash = url.substring(index + 1);
		
		RevWalk revWalk = new RevWalk(repository);
		ObjectId id=repository.resolve(commitHash);
		RevCommit commit=revWalk.parseCommit(id);
		revWalk.close();
		return commit;
	}
	
	private String getRepositoryURL(String url) {
		int index = url.lastIndexOf("/commit");
		if(index>=0)
			return url.substring(0,index);
		else
			return url;
	}
	
	private String getProjectName(String url) {
		int index = url.lastIndexOf("/");
		return url.substring(index + 1);
	}
	
	private Repository getRepository(String url) throws Exception {
		Repository rep = repositories.get(url);
		if(rep == null) {
			GitService gitService = new GitServiceImpl();
			String name = getProjectName(url);
			rep = gitService.cloneIfNotExists(folder + File.separator + name, url);
		}
		return rep;
	}
	
	private CSV getCSV(String url) throws IOException {
		CSV csv = CSVs.get(url);
		if(csv == null) {
			String name = url.replace("/", "м").replace(":", "из") +".csv";
			csv = new CSV(new File(csvFolder,name));
			CSVs.put(name, csv);
		}
		return csv;
	}
	
	public void close() throws IOException {
		for(CSV csv: CSVs.values())
			csv.close();
		for(Repository rep: repositories.values())
			rep.close();
	}

	public JsonHandler getJsonHandler() {
		return jHandler;
	}
	
	
}
