import java.util.Iterator;

/**
 * Represents a functional dependency, namely the dependent attributes
 * are determined by the independent set.
 *
 * Is mostly just an Immutable tuple with named fields.
 **/
public class FunctionalDependency {

	private final AttributeSet _independentAttributeSet;
	private final AttributeSet _dependentAttributeSet;
	//this FD represents independentSet -> dependentSet

	public FunctionalDependency(AttributeSet ind, AttributeSet dep) {
		_independentAttributeSet = new AttributeSet(ind);
		_dependentAttributeSet = new AttributeSet(dep);
	}

	public AttributeSet independent() {
		return new AttributeSet(_independentAttributeSet);
	}

	public AttributeSet dependent() {
		return new AttributeSet(_dependentAttributeSet);
	}
	
	public String toString() {
		String out = "";
		out += "Ind: ";
		out += _independentAttributeSet.toString();
		out += "\tDep: ";
		out += _dependentAttributeSet.toString();
		return out;
	}
}
