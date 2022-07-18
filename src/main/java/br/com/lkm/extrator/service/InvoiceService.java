package br.com.lkm.extrator.service;

import java.time.LocalDate;

import br.com.lkm.extrator.dto.CertificateDTO;
import br.com.lkm.extrator.dto.PageResponse;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface InvoiceService {

	@GET("invoices")
	public Call<ResponseBody> findInvoices(@Query("rootCnpj") String rootCnpj, @Query("cnpj") String cnpj, 
			@Query("startDate") LocalDate startData, @Query("endDate") LocalDate endDate, 
			@Query("provicneCode") String provinceCode, @Query("townhallUrl") String townhallUrl);

	@Multipart
	@POST("ocr")
	public Call<Void> ocr(@Part MultipartBody.Part file);

	@GET("certificates")
	public Call<PageResponse<CertificateDTO>> findAllCertificates(@Query("page") Integer page, @Query("size") Integer size);

	@GET("certificates/{cnpj}")
	public Call<CertificateDTO> get(@Path("cnpj") String cnpj);

	@Multipart
	@POST("certificates")
	public Call<Void> saveCertificate(@Part MultipartBody.Part file, @Query("id") Integer id, @Query("cnpj") String cnpj,
			@Query("name") String name,
			@Query("password") String password);
	
	@PUT("certificates/{cnpj}")
	public Call<Void> updateCertificatePassword(@Path("cnpj") String cnpj, @Query("password") String certificatePassword);

	@DELETE("certificates/{cnpj}")
	public Call<Void> deleteCertificate(@Path("cnpj") String cnpj);

	
	
}
