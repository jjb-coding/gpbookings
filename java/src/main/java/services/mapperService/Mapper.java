package services.mapperService;

public abstract class Mapper<T,U> {
	public abstract U invoke(T in);
	public abstract ClassPair describe();
}