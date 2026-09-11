package ui.component;


/**
 * Interface for all controls that can reflect data of some kind.
 */
public interface IReflectsData<T> {
	void setData(T data);
}