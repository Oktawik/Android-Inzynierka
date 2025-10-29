package com.example.pierwszawersjaapki;

import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Base64; // NOWY IMPORT
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.Manifest;
import android.widget.Toast;

import androidx.camera.core.Camera;
import java.io.File;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import com.example.pierwszawersjaapki.Gemini.GeminiMultimodalApiService;
import com.example.pierwszawersjaapki.Gemini.GeminiMultimodalRequest;
import com.example.pierwszawersjaapki.Gemini.GeminiMultimodalResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import okhttp3.OkHttpClient;
import java.util.concurrent.TimeUnit;

public class AparatFragment extends Fragment {

    private AlertDialog progressDialog;

    private ImageButton btn_gallery, btn_camera, btn_flashlight;
    private PreviewView previewView;
    private ImageCapture imageCapture;
    int cameraFacing = CameraSelector.LENS_FACING_BACK;
    private Camera camera; // dodane jako pole klasy

    private GeminiMultimodalApiService geminiApiService;
    private final String API_KEY = "AIzaSyC1su4bq1zcc5_ZtmJiMdTXZeyR4C_S4qQ";

    // Komunikacja z innym widokiem
    public static final String REQUEST_KEY_IMAGE_RESULT = "image_result_key";
    public static final String BUNDLE_KEY_TEXT = "gemini_text";
    public static final String BUNDLE_KEY_IMAGE_PATH = "image_path";

    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean o) {
            if (o) {
                startCamera(cameraFacing);
            }
        }
    });

    public AparatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aparat, container, false);

        previewView = view.findViewById(R.id.cameraPreview);
        btn_camera = view.findViewById(R.id.btn_camera);
        btn_gallery = view.findViewById(R.id.btn_gallery);
        btn_flashlight = view.findViewById(R.id.btn_flashlight);

        setupMultimodalRetrofit();

        btn_camera.setOnClickListener(v -> {
            if (imageCapture != null) {
                v.setEnabled(false); // Wyłącz przycisk podczas robienia zdjęcia i wysyłki
                takePicture(imageCapture);
            } else {
                Toast.makeText(requireActivity(), "Kamera nie jest gotowa", Toast.LENGTH_SHORT).show();
            }
        });

        btn_flashlight.setOnClickListener(v -> {
            if (camera != null) {
                setFlashIcon(camera);
            } else {
                Toast.makeText(requireActivity(), "Kamera nie jest gotowa", Toast.LENGTH_SHORT).show();
            }
        });

        // Sprawdzenie uprawnień kamery
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.CAMERA);
        } else {
            startCamera(cameraFacing);
        }

        return view;
    }

    // Metoda do inicjalizacji Retrofit dla GeminiMultimodalApiService
    private void setupMultimodalRetrofit() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS) // Limit czasu na połączenie
                .readTimeout(60, TimeUnit.SECONDS)    // Limit czasu na odczyt danych (odpowiedź)
                .writeTimeout(60, TimeUnit.SECONDS)   // Limit czasu na zapis danych (wysłanie Base64)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://generativelanguage.googleapis.com/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        geminiApiService = retrofit.create(GeminiMultimodalApiService.class);
    }

    private void startCamera(int cameraFacing) {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                int aspectRatio = aspectRatio(previewView.getWidth(), previewView.getHeight());

                Preview preview = new Preview.Builder()
                        .setTargetAspectRatio(aspectRatio)
                        .build();

                imageCapture = new ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .setTargetRotation(requireActivity().getWindowManager().getDefaultDisplay().getRotation())
                        .build();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(cameraFacing)
                        .build();

                cameraProvider.unbindAll();

                camera = cameraProvider.bindToLifecycle(requireActivity(), cameraSelector, preview, imageCapture);

                preview.setSurfaceProvider(previewView.getSurfaceProvider());

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }


    public void takePicture(ImageCapture imageCapture) {
        // ZMIENIONO: Plik zostanie zapisany w pamięci podręcznej aplikacji
        final File file = new File(requireActivity().getExternalFilesDir(null), System.currentTimeMillis() + ".jpg");
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
        imageCapture.takePicture(outputFileOptions, Executors.newCachedThreadPool(), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(ImageCapture.OutputFileResults outputFileResults) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireActivity(),"Obraz zapisany. Wysyłanie do Gemini...",Toast.LENGTH_SHORT).show();
                });

                // NOWE: Wyślij zapisane zdjęcie do Gemini
                sendImageToGemini(file);
                // W TEJ WERSJI NIE RESTARTUJEMY KAMERY NATYCHMIAST
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireActivity(), "Nie udało się zapisać zdjęcia "+exception.getMessage(), Toast.LENGTH_SHORT).show();
                    btn_camera.setEnabled(true); // Włącz przycisk z powrotem
                });
                startCamera(cameraFacing);
            }
        });
    }

    // NOWA: Metoda do konwersji pliku na Base64
    private String fileToBase64(File file) {
        try (InputStream inputStream = new FileInputStream(file)) {
            byte[] bytes;
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            bytes = buffer.toByteArray();
            // Użyj NO_WRAP, aby uniknąć znaków nowej linii
            return Base64.encodeToString(bytes, Base64.NO_WRAP);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // NOWA: Metoda do budowania żądania i wysyłania do API Gemini
    private void sendImageToGemini(File imageFile) {
        String base64Image = fileToBase64(imageFile);

        if (base64Image == null) {
            requireActivity().runOnUiThread(() -> {
                Toast.makeText(requireActivity(), "Błąd konwersji obrazu.", Toast.LENGTH_SHORT).show();
                btn_camera.setEnabled(true);
                startCamera(cameraFacing);
            });
            return;
        }

        // Zbuduj żądanie multimodalne
        String prompt = "Jaki obiekt przedstawia zdjęcie. Sama nazwa. Bez szczegółów. Jeśli jest to żywność to wypisz Kcal: Białko: Węglowodany: Tłuszcze";
        GeminiMultimodalRequest request = new GeminiMultimodalRequest(prompt, base64Image, "image/jpeg");

        // Wyślij żądanie
        geminiApiService.sendMultimodalMessage(request, API_KEY).enqueue(new Callback<GeminiMultimodalResponse>() {
            @Override
            public void onResponse(@NonNull Call<GeminiMultimodalResponse> call, @NonNull Response<GeminiMultimodalResponse> response) {
                requireActivity().runOnUiThread(() -> {
                    if (response.isSuccessful() && response.body() != null) {
                        String description = response.body().getTextOutput();

                        // ####### NOWA LOGIKA PRZEKAZYWANIA DANYCH #######

                        // 1. Spakuj dane do Bundle
                        Bundle result = new Bundle();
                        result.putString(BUNDLE_KEY_TEXT, description);
                        // Przekazujemy ścieżkę pliku, aby drugi fragment mógł go wczytać
                        result.putString(BUNDLE_KEY_IMAGE_PATH, imageFile.getAbsolutePath());

                        // 2. Ustaw wynik za pomocą Fragment Result API
                        getParentFragmentManager().setFragmentResult(REQUEST_KEY_IMAGE_RESULT, result);

                        // 3. Opcjonalnie: Przejdź do AparatResultFragment (jeśli używasz Navigation Component lub Activity)
                        // Jeśli używasz Activity do wymiany fragmentów, użyj tej logiki.
                        // Jeśli używasz Navigation Component, użyj navigate().

                        // Przykładowe użycie: Zmień fragment na AparatResultFragment
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.frame_layout, new AparatResultFragment()) // Zmień na ID Twojego kontenera
                                .addToBackStack(null) // Dodaj do stosu, aby można było wrócić
                                .commit();

                        // ###############################################

                    } else {
                        // ... (obsługa błędu)
                    }

                    // Po zakończeniu (sukces lub błąd), usuń plik i zrestartuj kamerę
                    // imageFile.delete();
                    btn_camera.setEnabled(true);
                    startCamera(cameraFacing);
                });
            }

            @Override
            public void onFailure(@NonNull Call<GeminiMultimodalResponse> call, @NonNull Throwable t) {
                requireActivity().runOnUiThread(() -> {
                    // Błąd sieciowy
                    Toast.makeText(requireActivity(), "Błąd sieci/API: " + t.getMessage(), Toast.LENGTH_LONG).show();

                    // Po błędzie, włącz przycisk i zrestartuj kamerę
                    imageFile.delete(); // Usuń plik po użyciu
                    btn_camera.setEnabled(true);
                    startCamera(cameraFacing);
                });
            }
        });
    }

    // ... setFlashIcon() i aspectRatio() pozostają bez zmian

    public void setFlashIcon(Camera camera) {
        if (camera.getCameraInfo().hasFlashUnit()) {
            if (camera.getCameraInfo().getTorchState().getValue() == 0) {
                camera.getCameraControl().enableTorch(true);
                btn_flashlight.setImageResource(R.drawable.ic_flashlight_off);
            } else {
                camera.getCameraControl().enableTorch(false);
                btn_flashlight.setImageResource(R.drawable.ic_flashlight_on);
            }
        } else {
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(requireActivity(), "Latarka jest niedostępna", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private int aspectRatio(int width, int height) {
        double previewRatio = (double) Math.max(width, height) / Math.min(width, height);
        if (Math.abs(previewRatio - 4.0 / 3.0) <= Math.abs(previewRatio - 16.0/9.0)) {
            return AspectRatio.RATIO_4_3;
        }
        return AspectRatio.RATIO_16_9;
    }
}