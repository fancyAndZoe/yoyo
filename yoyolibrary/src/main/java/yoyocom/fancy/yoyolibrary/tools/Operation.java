package yoyocom.fancy.yoyolibrary.tools;

/**
 *
 * Created by db on 15-6-19
 */
public interface Operation<E> {
	public Operation<E> setParams(Object... params);
	public Object[] getParams();
	public Object onBackgroundTask(Object... params);
	/**
	 *
	 * @param obj  返回结果
	 * @return  false 返回结果不符合要求  true返回结果没问题
	 */
	public boolean onPostExecute(ResponseResult obj);
	public void onTaskCancel();
	public void onTaskPreExecute();
}
