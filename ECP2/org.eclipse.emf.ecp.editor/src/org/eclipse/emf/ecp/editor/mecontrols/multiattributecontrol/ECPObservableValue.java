/**
 * 
 */
package org.eclipse.emf.ecp.editor.mecontrols.multiattributecontrol;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;

/**
 * @author Eugen Neufeld
 */
public class ECPObservableValue extends AbstractObservableValue
{

  private final IObservableList list;

  private int index;

  private final Object valueType;

  /**
   * @param list
   * @param index
   */
  public ECPObservableValue(IObservableList list, int index, Object valueType)
  {
    super();
    this.list = list;
    this.index = index;
    this.valueType = valueType;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.core.databinding.observable.value.IObservableValue#getValueType()
   */
  public Object getValueType()
  {
    return valueType;
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.core.databinding.observable.value.AbstractObservableValue#doGetValue()
   */
  @Override
  protected Object doGetValue()
  {
    return list.get(index);
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.core.databinding.observable.value.AbstractObservableValue#doSetValue(java.lang.Object)
   */
  @Override
  protected void doSetValue(Object value)
  {
    list.set(index, value);
  }

  /**
   * @return the index
   */
  public int getIndex()
  {
    return index;
  }

  /**
   * @param index
   *          the index to set
   */
  public void setIndex(int index)
  {
    this.index = index;
  }

}
